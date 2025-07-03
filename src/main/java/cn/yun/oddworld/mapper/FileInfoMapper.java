package cn.yun.oddworld.mapper;

import cn.yun.oddworld.entity.FileInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileInfoMapper {
    @Insert("INSERT INTO file_info (file_name, file_path, file_type, file_size, file_hash, user_id, status, system_status) " +
            "VALUES (#{fileName}, #{filePath}, #{fileType}, #{fileSize}, #{fileHash}, #{userId}, #{status}, #{systemStatus})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(FileInfo fileInfo);

    @Select("SELECT * FROM file_info WHERE id = #{id} AND user_id = #{userId} AND status = 2")
    FileInfo selectByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Select("SELECT * FROM file_info WHERE user_id = #{userId} AND status = 1")
    List<FileInfo> selectByUserId(@Param("userId") Long userId);

    @Update("UPDATE file_info SET status = 0 WHERE id = #{id} AND user_id = #{userId}")
    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Update("UPDATE file_info SET status = 2 WHERE id = #{id} AND user_id = #{userId}")
    int completeByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Select("SELECT * FROM file_info WHERE user_id = #{userId} AND status = 2 AND file_path=#{filePath} and (hidden is null or hidden = false) order by file_type='directory' desc, update_time desc")
    List<FileInfo> selectByPathAndUserId(@Param("filePath") String filePath, @Param("userId") Long userId);

    @Select("SELECT * FROM file_info WHERE user_id = #{userId} AND file_name = #{fileName} AND file_path = #{filePath} AND status = 1")
    FileInfo selectByUserIdAndFileNameAndPath(@Param("userId") Long userId, @Param("fileName") String fileName, @Param("filePath") String filePath);

    @Select("SELECT * FROM file_info WHERE user_id = #{userId} AND file_path LIKE CONCAT(#{prefix}, '%') AND status = 1")
    List<FileInfo> selectByUserIdAndPathPrefix(@Param("userId") Long userId, @Param("prefix") String prefix);

    @Select("SELECT * FROM file_info WHERE user_id = #{userId} AND file_path = #{parentId} AND status = 2")
    List<FileInfo> selectByUserIdAndParentId(@Param("userId") Long userId, @Param("parentId") String parentId);

    @Select("SELECT COUNT(*) FROM file_info WHERE user_id = #{userId} AND status = 2 AND file_type != 'directory'")
    int countNormalFiles(@Param("userId") Long userId);

    @Select("SELECT * FROM file_info WHERE user_id = #{userId} AND status = 2 AND file_type != 'directory'")
    List<FileInfo> selectNormalFiles(@Param("userId") Long userId);

    @Select("SELECT COUNT(*) FROM file_info WHERE user_id = #{userId} AND status = 2 AND file_type != 'directory' AND update_time >= #{fromTime}")
    int countRecentNormalFiles(@Param("userId") Long userId, @Param("fromTime") java.time.LocalDateTime fromTime);

    @Select("SELECT * FROM file_info WHERE user_id = #{userId} AND status = 2 AND file_type != 'directory' AND update_time >= #{fromTime} ORDER BY update_time DESC LIMIT 3")
    List<FileInfo> selectRecentFiles(@Param("userId") Long userId, @Param("fromTime") java.time.LocalDateTime fromTime);
} 