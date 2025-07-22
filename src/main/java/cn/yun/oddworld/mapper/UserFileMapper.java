package cn.yun.oddworld.mapper;

import cn.yun.oddworld.entity.UserFile;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface UserFileMapper {
    @Insert("INSERT INTO user_file (user_id, parent_id, file_name, physical_file_id, is_directory, file_category, status, created_at, updated_at, hidden) VALUES (#{userId}, #{parentId}, #{fileName}, #{physicalFileId}, #{isDirectory}, #{fileCategory}, #{status}, #{createdAt}, #{updatedAt}, #{hidden})")
    int insert(UserFile userFile);

    @Update("UPDATE user_file SET user_id=#{userId}, parent_id=#{parentId}, file_name=#{fileName}, physical_file_id=#{physicalFileId}, is_directory=#{isDirectory}, file_category=#{fileCategory}, status=#{status}, updated_at=#{updatedAt}, hidden=#{hidden} WHERE id=#{id}")
    int update(UserFile userFile);

    @Delete("DELETE FROM user_file WHERE id=#{id}")
    int deleteById(@Param("id") Long id);

    @Select("SELECT * FROM user_file WHERE id=#{id}")
    UserFile selectById(@Param("id") Long id);

    @Select("SELECT * FROM user_file WHERE user_id=#{userId} and status= 2")
    List<UserFile> selectByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM user_file WHERE user_id=#{userId} and parent_id = #{parentId} order by is_directory desc, updated_at desc")
    List<UserFile> selectByUserIdAndParentId(@Param("userId") Long userId, @Param("parentId") Long parentId);
} 