package cn.yun.oddworld.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FileInfo {
    private Long id;
    private String fileName;
    private String filePath;
    private String fileType;
    private Long fileSize;
    private String fileHash;
    private Long userId;
    private Integer status;
    private String uploadUrl;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer systemStatus;
    private Boolean hidden;
} 