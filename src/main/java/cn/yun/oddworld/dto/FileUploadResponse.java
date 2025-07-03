package cn.yun.oddworld.dto;

import lombok.Data;

@Data
public class FileUploadResponse {
    /**
     * 文件ID
     */
    private Long fileId;

    /**
     * 上传URL
     */
    private String uploadUrl;
} 