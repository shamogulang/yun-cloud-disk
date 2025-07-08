package cn.yun.oddworld.service.impl;

import cn.yun.oddworld.dto.FileCompleteRequest;
import cn.yun.oddworld.dto.FileUploadRequest;
import cn.yun.oddworld.entity.FileInfo;
import cn.yun.oddworld.repository.FileInfoRepository;
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
import java.util.List;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;
import cn.yun.oddworld.dto.FileInfoWithThumbnails;
import cn.yun.oddworld.dto.ThumbnailUrl;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final S3Client s3Client;
    private final FileInfoRepository repository;

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

            // Save file info to database
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(fileName);
            fileInfo.setFilePath(filePath);
            fileInfo.setFileType(fileType);
            fileInfo.setFileSize(fileSize);
            fileInfo.setUserId(userId);
            // TODO: Save to database
            repository.insert(fileInfo);
            return fileInfo;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    @Override
    public FileInfo prepareFile(FileUploadRequest fileUploadRequest) {
        // Save file info to database
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(fileUploadRequest.getFileName());
        fileInfo.setFilePath(fileUploadRequest.getFilePath());
        fileInfo.setFileType(fileUploadRequest.getFileType());
        fileInfo.setFileSize(fileUploadRequest.getFileSize());
        fileInfo.setFileHash(fileUploadRequest.getFileHash());
        fileInfo.setUserId(fileUploadRequest.getUserId());
        fileInfo.setStatus(fileUploadRequest.getStatus());
        repository.insert(fileInfo);
        String s3Key = ConfigConstant.generateKeyWithHash(String.valueOf(fileInfo.getId()), fileInfo.getFileHash());
        String uploadUrl = generateUploadUrl(s3Key);
        fileInfo.setUploadUrl(uploadUrl);
        fileInfo.setId(fileInfo.getId());
        return fileInfo;
    }

    @Override
    public void deleteFile(Long fileId, Long userId) {
        FileInfo fileInfo = repository.selectByIdAndUserId(fileId, userId);
        if (fileInfo == null) {
            throw new RuntimeException("File not found");
        }
        if (fileInfo.getSystemStatus() != null && fileInfo.getSystemStatus() == 1) {
            throw new RuntimeException("System file cannot be deleted");
        }

        // 基于 file_path 作为父ID 递归删除所有子文件和子目录
        if ("directory".equals(fileInfo.getFileType())) {
            String parentId = String.valueOf(fileInfo.getId());
            List<FileInfo> children = repository.selectByUserIdAndParentId(userId, parentId);
            for (FileInfo child : children) {
                deleteFile(child.getId(), userId);
            }
        }
        // 删除自己
        repository.deleteByIdAndUserId(fileId, userId);
    }

    @Override
    public FileInfo getFileInfo(Long fileId, Long userId) {
        return repository.selectByIdAndUserId(fileId, userId);
    }

    @Override
    public List<FileInfo> listFiles(Long userId, String path) {
        return repository.selectByPathAndUserId(userId, path);
    }

    @Override
    public String getFileUrl(Long fileId, Long userId) {
        FileInfo fileInfo = repository.selectByIdAndUserId(fileId, userId);
        String s3Key = fileInfo.getId()+"-"+ fileInfo.getFileHash();
        return generateDownloadUrl(s3Key);
    }

    @Override
    public void completeFile(FileCompleteRequest fileCompleteRequest) {
        repository.completeByIdAndUserId(fileCompleteRequest.getFileId(), fileCompleteRequest.getUserId());
        // 查询文件信息
        FileInfo fileInfo = repository.selectByIdAndUserId(fileCompleteRequest.getFileId(), fileCompleteRequest.getUserId());
        if (fileInfo != null && fileInfo.getFileType() != null && (fileInfo.getFileType().contains("image") || fileInfo.getFileType().contains("video"))) {
            try {
                String now = java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC).toString();
                String messageId = UUID.randomUUID().toString();
                TransferFileEvent event = new TransferFileEvent();
                event.setFullFileIdPath(fileInfo.getFilePath());
                event.setFileHash(fileInfo.getFileHash());
                event.setCreatedAt(fileInfo.getCreateTime() != null ? fileInfo.getCreateTime().toString() : now);
                event.setParentFileId(fileInfo.getFilePath()); // 需根据业务补充
                event.setFilePosition(fileInfo.getFilePath());
                event.setName(fileInfo.getFileName());
                event.setFileType(fileInfo.getFileType());
                event.setFileId(String.valueOf(fileInfo.getId()));
                event.setUpdatedAt(fileInfo.getUpdateTime() != null ? fileInfo.getUpdateTime().toString() : now);
                event.setEventTime(now);
                event.setEventType("file.created");
                event.setMessageId(messageId);
                event.setUserId(String.valueOf(fileInfo.getUserId()));
                
                // 生成 S3 下载地址
                String s3Key = ConfigConstant.generateKeyWithHash(String.valueOf(fileInfo.getId()) , fileInfo.getFileHash());
                String downloadUrl = generateDownloadUrl(s3Key);
                event.setDownloadUrl(downloadUrl);
                
                // 生成缩略图上传地址
                String hash = fileInfo.getFileHash();
                String baseName = fileInfo.getId() + "-" + hash;
                event.setBaseName(baseName);
                
                Map<String, String> thumbUploadUrls = generateThumbnailUploadUrls(baseName);
                event.setThumbUploadUrls(thumbUploadUrls);
                
                // 如果是视频类型，生成转码视频上传地址
                if (fileInfo.getFileType() != null && fileInfo.getFileType().contains("video")) {
                    Map<String, String> transcodedVideoUploadUrls = generateTranscodedVideoUploadUrls(baseName);
                    event.setTranscodedVideoUploadUrls(transcodedVideoUploadUrls);
                }
                
                String json = objectMapper.writeValueAsString(event);
                transferFileProducer.send("transfer_file", json);
            } catch (Exception e) {
                // 日志记录异常
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initializeUser(Long userId) {
        final String dirName = "我的应用收藏";
        final String path = "/";
        FileInfo existingDir = repository.selectByUserIdAndFileNameAndPath(userId, dirName, path);
        if (existingDir == null) {
            // 1. 再插入数据库
            FileInfo dirInfo = new FileInfo();
            dirInfo.setFileName(dirName);
            dirInfo.setFilePath(path);
            dirInfo.setFileType("directory");
            dirInfo.setFileSize(0L);
            dirInfo.setFileHash(""); // No hash for directory
            dirInfo.setUserId(userId);
            dirInfo.setStatus(2);
            dirInfo.setSystemStatus(1);
            repository.insert(dirInfo);
        }
    }

    @Override
    public int countNormalFiles(Long userId) {
        return repository.countNormalFiles(userId);
    }

    @Override
    public int countRecentNormalFiles(Long userId, java.time.LocalDateTime fromTime) {
        return repository.countRecentNormalFiles(userId, fromTime);
    }

    @Override
    public List<FileInfo> getRecentFiles(Long userId, java.time.LocalDateTime fromTime) {
        return repository.selectRecentFiles(userId, fromTime);
    }

    @Override
    public List<FileInfoWithThumbnails> listFilesWithThumbnails(Long userId, String path) {
        List<FileInfo> fileInfos = repository.selectByPathAndUserId(userId, path);
        List<FileInfoWithThumbnails> result = new java.util.ArrayList<>();
        for (FileInfo fileInfo : fileInfos) {
            FileInfoWithThumbnails withThumb = new FileInfoWithThumbnails();
            org.springframework.beans.BeanUtils.copyProperties(fileInfo, withThumb);
            if (fileInfo.getFileType() != null && (fileInfo.getFileType().contains("image") || fileInfo.getFileType().contains("video"))) {
                String hash = fileInfo.getFileHash();
                String baseName = fileInfo.getId() + "-" + hash;
                java.util.List<ThumbnailUrl> thumbnailUrls = new java.util.ArrayList<>();
                for (String size : new String[]{"L", "M", "S"}) {
                    String thumbKey = baseName + "-thumb-" + size + ".jpg";
                    String url = generateDownloadUrl(thumbKey);
                    thumbnailUrls.add(new ThumbnailUrl(size, url));
                }
                withThumb.setThumbnailUrls(thumbnailUrls);
            } else {
                withThumb.setThumbnailUrls(java.util.Collections.emptyList());
            }
            result.add(withThumb);
        }
        return result;
    }

    /**
     * 生成 S3 上传地址
     */
    private String generateUploadUrl(String s3Key) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(s3Key)
                .build();
        software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest putPresignRequest =
                software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(60))
                        .putObjectRequest(putObjectRequest)
                        .build();
        S3Presigner presigner = createS3Presigner();
        return presigner.presignPutObject(putPresignRequest).url().toString();
    }

    /**
     * 生成 S3 下载地址
     */
    private String generateDownloadUrl(String s3Key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(s3Key)
                .build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60))
                .getObjectRequest(getObjectRequest)
                .build();
        S3Presigner presigner = createS3Presigner();
        return presigner.presignGetObject(presignRequest).url().toString();
    }

    /**
     * 生成缩略图上传地址
     */
    private Map<String, String> generateThumbnailUploadUrls(String baseName) {
        String ext = ".jpg";
        String[] sizes = {"S", "M", "L"};
        Map<String, String> thumbUploadUrls = new HashMap<>();
        S3Presigner presigner = createS3Presigner();
        
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
        S3Presigner presigner = createS3Presigner();
        
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
    private S3Presigner createS3Presigner() {
        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .endpointOverride(URI.create(endpoint))
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
        FileInfo fileInfo = repository.selectByIdAndUserId(fileId, userId);
        if (fileInfo == null || fileInfo.getFileType() == null || !fileInfo.getFileType().contains("video")) {
            throw new RuntimeException("该文件不是视频类型");
        }
        String s3Key = fileInfo.getId() + "-" + fileInfo.getFileHash()+ "-transcode-"+resolution+".mp4";
        // 可根据业务调整为转码后的视频key
        return generateDownloadUrl(s3Key);
    }

    @Override
    public String getImagePreviewUrl(Long fileId, Long userId) {
        FileInfo fileInfo = repository.selectByIdAndUserId(fileId, userId);
        if (fileInfo == null || fileInfo.getFileType() == null || !fileInfo.getFileType().contains("image")) {
            throw new RuntimeException("该文件不是图片类型");
        }
        String s3Key = fileInfo.getId() + "-" + fileInfo.getFileHash();
        // 可根据业务调整为缩略图key
        return generateDownloadUrl(s3Key);
    }
}