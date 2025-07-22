package cn.yun.oddworld.dto;

import lombok.Data;
import java.util.List;

@Data
public class FileInfoWithThumbnails extends FileInfo {
    private List<ThumbnailUrl> thumbnailUrls;
} 