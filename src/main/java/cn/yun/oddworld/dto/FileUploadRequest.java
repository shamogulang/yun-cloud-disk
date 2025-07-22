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

    /**
     * 父目录ID（0为根目录）
     */
    private Long parentId;

    /**
     * 文件分类：image, video, document, audio, archive, other
     */
    private String fileCategory;

    private Integer status;
} 