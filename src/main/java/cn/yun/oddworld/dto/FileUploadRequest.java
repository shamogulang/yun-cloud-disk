package cn.yun.oddworld.dto;

import lombok.Data;

@Data
public class FileUploadRequest {
    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件哈希值
     */
    private String fileHash;

    private Integer status;
} 