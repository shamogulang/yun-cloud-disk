package cn.yun.oddworld.repository;

import cn.yun.oddworld.entity.FileInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FileInfoRepository {

    int insert(FileInfo fileInfo);

    FileInfo selectByIdAndUserId(Long id, Long userId);

    List<FileInfo> selectByUserId(Long userId);


    List<FileInfo> selectByPathAndUserId(Long userId, String path);

    int deleteByIdAndUserId(Long id, Long userId);

    int completeByIdAndUserId( Long id,  Long userId);

    FileInfo selectByUserIdAndFileNameAndPath(Long userId, String fileName, String filePath);

    List<FileInfo> selectByUserIdAndPathPrefix(Long userId, String prefix);

    List<FileInfo> selectByUserIdAndParentId(Long userId, String parentId);

    int countNormalFiles(Long userId);

    List<FileInfo> selectNormalFiles(Long userId);

    int countRecentNormalFiles(Long userId, java.time.LocalDateTime fromTime);

    List<FileInfo> selectRecentFiles(Long userId, java.time.LocalDateTime fromTime);
}
