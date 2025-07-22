package cn.yun.oddworld.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserFile {
    private Long id;
    private Long userId;
    private Long parentId;
    private String fileName;
    private Long physicalFileId;
    private Boolean isDirectory;
    private String fileCategory;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean hidden;
} 