package cn.yun.oddworld.mq;

import lombok.Data;
import java.util.Map;

@Data
public class TransferFileEvent {
    private String fullFileIdPath;
    private String fileHash;
    private String createdAt;
    private String parentFileId;
    private String filePosition;
    private String name;
    private String fileType;
    private String fileId;
    private String updatedAt;
    private String eventTime;
    private String eventType;
    private String messageId;
    private String userId;
    private String downloadUrl;
    private Map<String, String> thumbUploadUrls;
    private String baseName;
    private Map<String, String> transcodedVideoUploadUrls;
}