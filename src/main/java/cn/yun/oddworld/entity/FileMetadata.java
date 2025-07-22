package cn.yun.oddworld.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FileMetadata {
    private Long id;
    private Long physicalFileId;
    private LocalDateTime cameraTakenAt;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Integer videoDuration;
    private String metadataJson;
    private LocalDateTime createdAt;
} 