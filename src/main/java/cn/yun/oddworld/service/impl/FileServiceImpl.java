package cn.yun.oddworld.service.impl;

import cn.yun.oddworld.cache.StorageBucketCache;
import cn.yun.oddworld.dto.FileCompleteRequest;
import cn.yun.oddworld.dto.FileUploadRequest;
import cn.yun.oddworld.dto.FileInfo;
import cn.yun.oddworld.entity.StorageBucket;
import cn.yun.oddworld.entity.UserFile;
import cn.yun.oddworld.entity.PhysicalFile;
import cn.yun.oddworld.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import org.springframework.beans.factory.annotation.Autowired;
import cn.yun.oddworld.rocketmq.TransferFileProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.yun.oddworld.mq.TransferFileEvent;
import cn.yun.oddworld.ConfigConstant;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import java.net.URI;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

import cn.yun.oddworld.dto.FileInfoWithThumbnails;
import cn.yun.oddworld.dto.ThumbnailUrl;
import cn.yun.oddworld.mapper.UserFileMapper;
import cn.yun.oddworld.mapper.PhysicalFileMapper;
import cn.yun.oddworld.mapper.FileMetadataMapper;
import cn.yun.oddworld.mapper.FileDerivativeMapper;
import cn.yun.oddworld.mapper.StorageBucketMapper;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final S3Client s3Client;
    private final UserFileMapper userFileMapper;
    private final PhysicalFileMapper physicalFileMapper;
    private final FileMetadataMapper fileMetadataMapper;
    private final FileDerivativeMapper fileDerivativeMapper;
    private final StorageBucketMapper storageBucketMapper;
    private final cn.yun.oddworld.cache.StorageBucketCache storageBucketCache;

    @Value("${aws.s3.bucket}")
    private String bucket;



    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.access-key}")
    private String accessKey;

    @Value("${aws.s3.secret-key}")
    private String secretKey;

    @Value("${aws.s3.endpoint}")
    private String endpoint;

    @Autowired
    private TransferFileProducer transferFileProducer;
    @Autowired
    private ObjectMapper objectMapper;

    private FileInfo mapToFileInfo(UserFile userFile, PhysicalFile physical) {
        FileInfo dto = new FileInfo();
        dto.setId(userFile.getId());
        dto.setUserId(userFile.getUserId());
        dto.setParentId(userFile.getParentId());
        dto.setFileName(userFile.getFileName());
        dto.setIsDirectory(userFile.getIsDirectory());
        dto.setFileCategory(userFile.getFileCategory());
        dto.setStatus(userFile.getStatus());
        dto.setCreatedAt(userFile.getCreatedAt());
        dto.setUpdatedAt(userFile.getUpdatedAt());
        dto.setHidden(userFile.getHidden());
        if (Boolean.FALSE.equals(userFile.getIsDirectory()) && physical != null) {
            dto.setFileSize(physical.getFileSize());
            dto.setFileType(physical.getFileType());
            dto.setFileHash(physical.getFileHash());
        }
        return dto;
    }

    private FileInfoWithThumbnails mapToFileInfoWithThumbnails(UserFile userFile, PhysicalFile physical) {
        FileInfoWithThumbnails dto = new FileInfoWithThumbnails();
        FileInfo base = mapToFileInfo(userFile, physical);
        org.springframework.beans.BeanUtils.copyProperties(base, dto);
        if (physical != null && physical.getFileType() != null && (physical.getFileType().contains("image") || physical.getFileType().contains("video"))) {
            StorageBucket storageBucket =  storageBucketCache.getStorageBucketMap().get(physical.getStorageId());
            String baseName = physical.getId() + "-" + physical.getFileHash();
            java.util.List<ThumbnailUrl> thumbnailUrls = new java.util.ArrayList<>();
            for (String size : new String[]{"L", "M", "S"}) {
                String thumbKey = baseName + "-thumb-" + size + ".jpg";
                String url = generateDownloadUrl(thumbKey, storageBucket);
                thumbnailUrls.add(new ThumbnailUrl(size, url));
            }
            dto.setThumbnailUrls(thumbnailUrls);
        } else {
            dto.setThumbnailUrls(java.util.Collections.emptyList());
        }
        return dto;
    }

    @Override
    public FileInfo uploadFile(MultipartFile file, Long userId) {
        try {
            String fileName = file.getOriginalFilename();
            String fileType = file.getContentType();
            long fileSize = file.getSize();
            String filePath = userId + "/" + UUID.randomUUID().toString() + "/" + fileName;

            // Upload to S3
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(filePath)
                    .contentType(fileType)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), fileSize));

            // Create physical file entry
            PhysicalFile physical = new PhysicalFile();
            physical.setFileHash(filePath); // Use filePath as hash for now
            physical.setFileSize(fileSize);
            physical.setFileType(fileType);
            physical.setS3Path(filePath);
            physical.setReferenceCount(1);
            physical.setStorageId(1L); // Default storage ID
            physical.setCreatedAt(java.time.LocalDateTime.now());
            physicalFileMapper.insert(physical);

            // Save user file info to database
            UserFile userFile = new UserFile();
            userFile.setUserId(userId);
            userFile.setFileName(fileName);
            userFile.setPhysicalFileId(physical.getId());
            userFile.setIsDirectory(false);
            userFile.setFileCategory("other");
            userFile.setStatus(2); // Default status for uploaded file
            userFile.setCreatedAt(java.time.LocalDateTime.now());
            userFile.setUpdatedAt(java.time.LocalDateTime.now());
            userFile.setHidden(false);
            userFileMapper.insert(userFile);

            return mapToFileInfo(userFile, physical);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    @Override
    public FileInfo prepareFile(FileUploadRequest fileUploadRequest) {
        if ("directory".equals(fileUploadRequest.getFileType())) {
            UserFile dir = new UserFile();
            dir.setUserId(fileUploadRequest.getUserId());
            dir.setParentId(fileUploadRequest.getParentId());
            dir.setFileName(fileUploadRequest.getFileName());
            dir.setIsDirectory(true);
            dir.setFileCategory(fileUploadRequest.getFileCategory());
            dir.setStatus(fileUploadRequest.getStatus());
            dir.setCreatedAt(java.time.LocalDateTime.now());
            dir.setPhysicalFileId(0L);
            dir.setUpdatedAt(java.time.LocalDateTime.now());
            dir.setHidden(false);
            userFileMapper.insert(dir);
            return mapToFileInfo(dir, null);
        } else {
            PhysicalFile physical = physicalFileMapper.selectByHash(fileUploadRequest.getFileHash());
            FileInfo dto;
            if (physical == null) {
                // 物理文件不存在，生成物理文件记录和上传地址
                physical = new PhysicalFile();
                physical.setFileHash(fileUploadRequest.getFileHash());
                physical.setFileSize(fileUploadRequest.getFileSize());
                physical.setFileType(fileUploadRequest.getFileType());
                physical.setReferenceCount(1);
                physical.setS3Path("");
                physical.setStorageId(storageBucketCache.getDefaultBucket().getId());
                physical.setCreatedAt(java.time.LocalDateTime.now());
                physicalFileMapper.insert(physical);

                // S3 key = 物理文件ID + fileHash
                String s3Key = physical.getId() + "-" + physical.getFileHash();
                StorageBucket storageBucket = storageBucketCache.getStorageBucketMap().get(physical.getStorageId());
                String uploadUrl = generateUploadUrl(s3Key, storageBucket);

                UserFile userFile = new UserFile();
                userFile.setUserId(fileUploadRequest.getUserId());
                userFile.setParentId(fileUploadRequest.getParentId());
                userFile.setFileName(fileUploadRequest.getFileName());
                userFile.setPhysicalFileId(physical.getId());
                userFile.setIsDirectory(false);
                userFile.setFileCategory(fileUploadRequest.getFileCategory());
                userFile.setStatus(fileUploadRequest.getStatus());
                userFile.setCreatedAt(java.time.LocalDateTime.now());
                userFile.setUpdatedAt(java.time.LocalDateTime.now());
                userFile.setHidden(false);
                userFileMapper.insert(userFile);

                dto = mapToFileInfo(userFile, physical);
                dto.setUploadUrl(uploadUrl); // 返回上传地址
            } else {
                // 物理文件已存在，走秒传
                physical.setReferenceCount(physical.getReferenceCount() + 1);
                physicalFileMapper.update(physical);

                UserFile userFile = new UserFile();
                userFile.setUserId(fileUploadRequest.getUserId());
                userFile.setParentId(fileUploadRequest.getParentId());
                userFile.setFileName(fileUploadRequest.getFileName());
                userFile.setPhysicalFileId(physical.getId());
                userFile.setIsDirectory(false);
                userFile.setFileCategory(fileUploadRequest.getFileCategory());
                userFile.setStatus(fileUploadRequest.getStatus());
                userFile.setCreatedAt(java.time.LocalDateTime.now());
                userFile.setUpdatedAt(java.time.LocalDateTime.now());
                userFile.setHidden(false);
                userFileMapper.insert(userFile);

                dto = mapToFileInfo(userFile, physical);
                // 不返回uploadUrl
            }
            return dto;
        }
    }

    @Override
    public void deleteFile(Long fileId, Long userId) {
        UserFile userFile = userFileMapper.selectById(fileId);
        if (userFile == null || !userFile.getUserId().equals(userId)) {
            throw new RuntimeException("File not found");
        }
        if (Boolean.TRUE.equals(userFile.getIsDirectory())) {
            List<UserFile> children = userFileMapper.selectByUserId(userId);
            for (UserFile child : children) {
                if (child.getParentId() != null && child.getParentId().equals(fileId)) {
                    deleteFile(child.getId(), userId);
                }
            }
        } else if (userFile.getPhysicalFileId() != null) {
            PhysicalFile physical = physicalFileMapper.selectById(userFile.getPhysicalFileId());
            if (physical != null) {
                int refCount = physical.getReferenceCount() - 1;
                if (refCount <= 0) {
                    physicalFileMapper.deleteById(physical.getId());
                } else {
                    physical.setReferenceCount(refCount);
                    physicalFileMapper.update(physical);
                }
            }
        }
        userFileMapper.deleteById(fileId);
    }

    @Override
    public FileInfo getFileInfo(Long fileId, Long userId) {
        UserFile userFile = userFileMapper.selectById(fileId);
        if (userFile == null || !userFile.getUserId().equals(userId)) {
            throw new RuntimeException("File not found");
        }
        PhysicalFile physical = userFile.getPhysicalFileId() != null ? physicalFileMapper.selectById(userFile.getPhysicalFileId()) : null;
        return mapToFileInfo(userFile, physical);
    }

    @Override
    public List<FileInfo> listFiles(Long userId, String path) {
        List<UserFile> userFiles = userFileMapper.selectByUserId(userId);
        List<FileInfo> result = new java.util.ArrayList<>();
        for (UserFile userFile : userFiles) {
            PhysicalFile physical = userFile.getPhysicalFileId() != null ? physicalFileMapper.selectById(userFile.getPhysicalFileId()) : null;
            result.add(mapToFileInfo(userFile, physical));
        }
        return result;
    }

    @Override
    public String getFileUrl(Long fileId, Long userId) {
        UserFile userFile = userFileMapper.selectById(fileId);
        if (userFile == null || !userFile.getUserId().equals(userId)) {
            throw new RuntimeException("File not found");
        }
        if (userFile.getPhysicalFileId() == null) {
            throw new RuntimeException("No physical file for this user file");
        }
        PhysicalFile physical = physicalFileMapper.selectById(userFile.getPhysicalFileId());
        if (physical == null) {
            throw new RuntimeException("Physical file not found");
        }
        StorageBucket storageBucket = storageBucketCache.getStorageBucketMap().get(physical.getStorageId());

        String s3Key = physical.getId() + "-" + physical.getFileHash();
        return generateDownloadUrl(s3Key, storageBucket);
    }

    @Override
    public void completeFile(FileCompleteRequest fileCompleteRequest) {
        // No-op for now, or update status if needed
        UserFile userFile = userFileMapper.selectById(fileCompleteRequest.getFileId());
        if (userFile != null && userFile.getUserId().equals(fileCompleteRequest.getUserId())) {
            userFile.setStatus(2); // Mark as complete
            userFileMapper.update(userFile);
        }
    }

    @Override
    public void initializeUser(Long userId) {
        final String dirName = "我的应用收藏";
        List<UserFile> existingDirs = userFileMapper.selectByUserId(userId);
        boolean exists = existingDirs.stream().anyMatch(f -> Boolean.TRUE.equals(f.getIsDirectory()) && dirName.equals(f.getFileName()) && (f.getParentId() == null));
        if (!exists) {
            UserFile dir = new UserFile();
            dir.setUserId(userId);
            dir.setParentId(0L);
            dir.setFileName(dirName);
            dir.setIsDirectory(true);
            // if the file is a directory, this field default value is empty
            dir.setFileCategory("");
            dir.setStatus(2);
            dir.setCreatedAt(java.time.LocalDateTime.now());
            dir.setUpdatedAt(java.time.LocalDateTime.now());
            dir.setPhysicalFileId(0L);
            dir.setHidden(false);
            userFileMapper.insert(dir);
        }
    }

    @Override
    public int countNormalFiles(Long userId) {
        List<UserFile> files = userFileMapper.selectByUserId(userId);
        return (int) files.stream().filter(f -> f.getStatus() != null && f.getStatus() == 2).count();
    }

    @Override
    public int countRecentNormalFiles(Long userId, java.time.LocalDateTime fromTime) {
        List<UserFile> files = userFileMapper.selectByUserId(userId);
        return (int) files.stream().filter(f -> f.getStatus() != null && f.getStatus() == 2 && f.getCreatedAt() != null && f.getCreatedAt().isAfter(fromTime)).count();
    }

    @Override
    public List<FileInfo> getRecentFiles(Long userId, java.time.LocalDateTime fromTime) {
        List<UserFile> files = userFileMapper.selectByUserId(userId);
        List<FileInfo> recent = new java.util.ArrayList<>();
        for (UserFile f : files) {
            if (f.getCreatedAt() != null && f.getCreatedAt().isAfter(fromTime)) {
                PhysicalFile physical = f.getPhysicalFileId() != null ? physicalFileMapper.selectById(f.getPhysicalFileId()) : null;
                recent.add(mapToFileInfo(f, physical));
            }
        }
        return recent;
    }

    @Override
    public List<FileInfoWithThumbnails> listFilesWithThumbnails(Long userId, Long parentId) {
        List<UserFile> userFiles = userFileMapper.selectByUserIdAndParentId(userId, parentId);
        List<FileInfoWithThumbnails> result = new java.util.ArrayList<>();
        for (UserFile userFile : userFiles) {
            PhysicalFile physical = userFile.getPhysicalFileId() != null ? physicalFileMapper.selectById(userFile.getPhysicalFileId()) : null;
            result.add(mapToFileInfoWithThumbnails(userFile, physical));
        }
        return result;
    }

    @Override
    public List<FileInfoWithThumbnails> listFilesWithThumbnails(Long userId, List<Long> fileIds) {
        List<UserFile> userFiles = userFileMapper.selectByUserIdAndFileIds(userId, fileIds);
        List<FileInfoWithThumbnails> result = new java.util.ArrayList<>();
        for (UserFile userFile : userFiles) {
            PhysicalFile physical = userFile.getPhysicalFileId() != null ? physicalFileMapper.selectById(userFile.getPhysicalFileId()) : null;
            result.add(mapToFileInfoWithThumbnails(userFile, physical));
        }
        return result;
    }

    /**
     * 生成 S3 上传地址
     */
    private String generateUploadUrl(String s3Key, StorageBucket storageBucket) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(storageBucket.getBucketName())
                .key(s3Key)
                .build();
        software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest putPresignRequest =
                software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(60))
                        .putObjectRequest(putObjectRequest)
                        .build();
        S3Presigner presigner = createS3Presigner(storageBucket);
        return presigner.presignPutObject(putPresignRequest).url().toString();
    }

    /**
     * 生成 S3 下载地址
     */
    private String generateDownloadUrl(String s3Key, StorageBucket storageBucket) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(storageBucket.getBucketName())
                .key(s3Key)
                .build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60))
                .getObjectRequest(getObjectRequest)
                .build();
        S3Presigner presigner = createS3Presigner(storageBucket);
        return presigner.presignGetObject(presignRequest).url().toString();
    }

    /**
     * 生成缩略图上传地址
     */
    private Map<String, String> generateThumbnailUploadUrls(String baseName) {
        String ext = ".jpg";
        String[] sizes = {"S", "M", "L"};
        Map<String, String> thumbUploadUrls = new HashMap<>();
        S3Presigner presigner = createS3Presigner(null);
        
        for (String size : sizes) {
            String thumbKey = baseName + "-thumb-" + size + ext;
            PutObjectRequest thumbPutObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(thumbKey)
                    .build();
            software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest thumbPutPresignRequest =
                    software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest.builder()
                            .signatureDuration(Duration.ofMinutes(60))
                            .putObjectRequest(thumbPutObjectRequest)
                            .build();
            String thumbUploadUrl = presigner.presignPutObject(thumbPutPresignRequest).url().toString();
            thumbUploadUrls.put(size, thumbUploadUrl);
        }
        return thumbUploadUrls;
    }

    /**
     * 生成转码视频上传地址
     */
    private Map<String, String> generateTranscodedVideoUploadUrls(String baseName) {
        String ext = ".mp4";
        String[] videoSizes = {"1080", "720", "480"};
        Map<String, String> transcodedVideoUploadUrls = new HashMap<>();
        S3Presigner presigner = createS3Presigner(null);
        
        for (String size : videoSizes) {
            String videoKey = baseName + "-transcode-" + size + ext;
            PutObjectRequest videoPutObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(videoKey)
                    .build();
            software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest videoPutPresignRequest =
                    software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest.builder()
                            .signatureDuration(Duration.ofMinutes(60))
                            .putObjectRequest(videoPutObjectRequest)
                            .build();
            String videoUploadUrl = presigner.presignPutObject(videoPutPresignRequest).url().toString();
            transcodedVideoUploadUrls.put(size, videoUploadUrl);
        }
        return transcodedVideoUploadUrls;
    }

    /**
     * 创建 S3Presigner 实例
     */
    private S3Presigner createS3Presigner(StorageBucket storageBucket) {
        return S3Presigner.builder()
                .region(Region.of(storageBucket.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(storageBucket.getAccessKey(), storageBucket.getSecretKey())))
                .endpointOverride(URI.create(storageBucket.getEndpoint()))
                .build();
    }

    @Override
    public List<String> getFileUrls(List<Long> fileIds, Long userId) {
        List<String> urls = new java.util.ArrayList<>();
        if (fileIds != null) {
            for (Long fileId : fileIds) {
                urls.add(getFileUrl(fileId, userId));
            }
        }
        return urls;
    }

    @Override
    public String getVideoPlayUrl(Long fileId, Long userId,Long resolution) {
        UserFile userFile = userFileMapper.selectById(fileId);
        if (userFile == null || userFile.getPhysicalFileId() == null) {
            throw new RuntimeException("File not found or no physical file");
        }
        PhysicalFile physical = physicalFileMapper.selectById(userFile.getPhysicalFileId());
        if (physical == null || !physical.getFileType().contains("video")) {
            throw new RuntimeException("该文件不是视频类型");
        }
        StorageBucket storageBucket = storageBucketCache.getStorageBucketMap().get(physical.getStorageId());
        String s3Key = physical.getId() + "-" + physical.getFileHash()+ "-transcode-"+resolution+".mp4";
        // 可根据业务调整为转码后的视频key
        return generateDownloadUrl(s3Key, storageBucket);
    }

    @Override
    public String getImagePreviewUrl(Long fileId, Long userId) {
        UserFile userFile = userFileMapper.selectById(fileId);
        if (userFile == null || userFile.getPhysicalFileId() == null) {
            throw new RuntimeException("File not found or no physical file");
        }
        PhysicalFile physical = physicalFileMapper.selectById(userFile.getPhysicalFileId());
        if (physical == null || !physical.getFileType().contains("image")) {
            throw new RuntimeException("该文件不是图片类型");
        }
        StorageBucket storageBucket = storageBucketCache.getStorageBucketMap().get(physical.getStorageId());
        String s3Key = physical.getId() + "-" + physical.getFileHash();
        // 可根据业务调整为缩略图key
        return generateDownloadUrl(s3Key, storageBucket);
    }
}