package cn.yun.oddworld.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StorageBucket {
    private Long id;
    private String name;
    private String provider;
    private String bucketName;
    private String region;
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private Boolean isDefault;
    private LocalDateTime createdAt;
    private Integer status; // 新增字段，表示存储桶状态
} 