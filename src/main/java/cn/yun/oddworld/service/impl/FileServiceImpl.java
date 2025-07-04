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
        String s3Key = ConfigConstant.generateKeyWithHash(fileInfo.getFileName(), fileInfo.getFileHash());
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(s3Key)
                .build();
        software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest putPresignRequest =
                software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(60))
                        .putObjectRequest(putObjectRequest)
                        .build();
        S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .endpointOverride(URI.create(endpoint))
                .build();
        String uploadUrl = presigner.presignPutObject(putPresignRequest).url().toString();
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
        // TODO: Get file info from database
        FileInfo fileInfo = new FileInfo();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(fileInfo.getFilePath())
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60))
                .getObjectRequest(getObjectRequest)
                .build();

        S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .endpointOverride(URI.create(endpoint))
                .build();

        return presigner.presignGetObject(presignRequest).url().toString();
    }

    @Override
    public void completeFile(FileCompleteRequest fileCompleteRequest) {
        repository.completeByIdAndUserId(fileCompleteRequest.getFileId(), fileCompleteRequest.getUserId());
        // 查询文件信息
        FileInfo fileInfo = repository.selectByIdAndUserId(fileCompleteRequest.getFileId(), fileCompleteRequest.getUserId());
        if (fileInfo != null && fileInfo.getFileType() != null && fileInfo.getFileType().contains("image")) {
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
                // 生成 S3 上传地址
                String s3Key = ConfigConstant.generateKeyWithHash(fileInfo.getFileName(), fileInfo.getFileHash());
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(s3Key)
                        .build();
                software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest putPresignRequest =
                        software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest.builder()
                                .signatureDuration(Duration.ofMinutes(60))
                                .putObjectRequest(putObjectRequest)
                                .build();
                S3Presigner presigner = S3Presigner.builder()
                        .region(Region.of(region))
                        .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                        .endpointOverride(URI.create(endpoint))
                        .build();
                // 生成 S3 下载地址
                GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(s3Key)
                        .build();
                GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(60))
                        .getObjectRequest(getObjectRequest)
                        .build();
                String downloadUrl = presigner.presignGetObject(presignRequest).url().toString();
                event.setDownloadUrl(downloadUrl);
                // 生成缩略图上传地址
                String fileName = fileInfo.getFileName();
                String hash = fileInfo.getFileHash();
                int extIndex = fileName.lastIndexOf('.');
                String baseName = (extIndex != -1) ? fileName.substring(0, extIndex) : fileName;
                baseName = baseName + "-" + hash + "-thumb-";
                event.setBaseName(baseName);
                String ext = ".jpg";
                String[] sizes = {"S", "M", "L"};
                Map<String, String> thumbUploadUrls = new HashMap<>();
                for (String size : sizes) {
                    String thumbKey = baseName  + size + ext;
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
                event.setThumbUploadUrls(thumbUploadUrls);
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
}