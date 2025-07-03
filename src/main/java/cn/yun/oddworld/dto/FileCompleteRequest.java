package cn.yun.oddworld.dto;

import lombok.Data;

@Data
public class FileCompleteRequest {

    private Long fileId;
    private Long userId;
}
