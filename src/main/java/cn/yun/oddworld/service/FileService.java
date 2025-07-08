package cn.yun.oddworld.service;

import cn.yun.oddworld.dto.FileCompleteRequest;
import cn.yun.oddworld.dto.FileUploadRequest;
import cn.yun.oddworld.entity.FileInfo;
import cn.yun.oddworld.dto.FileInfoWithThumbnails;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface FileService {
    FileInfo uploadFile(MultipartFile file, Long userId);

    FileInfo prepareFile(FileUploadRequest fileUploadRequest);

    void completeFile(FileCompleteRequest fileCompleteRequest);

    void deleteFile(Long fileId, Long userId);
    FileInfo getFileInfo(Long fileId, Long userId);
    List<FileInfo> listFiles(Long userId, String path);
    String getFileUrl(Long fileId, Long userId);
    List<String> getFileUrls(List<Long> fileIds, Long userId);
    String getVideoPlayUrl(Long fileId, Long userId,Long resolution);
    String getImagePreviewUrl(Long fileId, Long userId);
    void initializeUser(Long userId);
    int countNormalFiles(Long userId);
    int countRecentNormalFiles(Long userId, java.time.LocalDateTime fromTime);
    List<FileInfo> getRecentFiles(Long userId, java.time.LocalDateTime fromTime);
    List<FileInfoWithThumbnails> listFilesWithThumbnails(Long userId, String path);
} 