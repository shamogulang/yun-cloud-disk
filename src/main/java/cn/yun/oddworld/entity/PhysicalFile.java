package cn.yun.oddworld.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PhysicalFile {
    private Long id;
    private String fileHash;
    private Long fileSize;
    private String fileType;
    private String s3Path;
    private Integer referenceCount;
    private Long storageId;
    private LocalDateTime createdAt;
} 