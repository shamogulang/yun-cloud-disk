package cn.yun.oddworld.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FileDerivative {
    private Long id;
    private Long physicalFileId;
    private String type;
    private Integer size;
    private String s3Path;
    private String format;
    private LocalDateTime createdAt;
} 