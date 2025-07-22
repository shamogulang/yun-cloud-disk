package cn.yun.oddworld.mapper;

import cn.yun.oddworld.entity.FileDerivative;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface FileDerivativeMapper {
    @Insert("INSERT INTO file_derivative (physical_file_id, type, size, s3_path, format, created_at) VALUES (#{physicalFileId}, #{type}, #{size}, #{s3Path}, #{format}, #{createdAt})")
    int insert(FileDerivative fileDerivative);

    @Update("UPDATE file_derivative SET physical_file_id=#{physicalFileId}, type=#{type}, size=#{size}, s3_path=#{s3Path}, format=#{format} WHERE id=#{id}")
    int update(FileDerivative fileDerivative);

    @Delete("DELETE FROM file_derivative WHERE id=#{id}")
    int deleteById(@Param("id") Long id);

    @Select("SELECT * FROM file_derivative WHERE id=#{id}")
    FileDerivative selectById(@Param("id") Long id);

    @Select("SELECT * FROM file_derivative WHERE physical_file_id=#{physicalFileId}")
    List<FileDerivative> selectByPhysicalFileId(@Param("physicalFileId") Long physicalFileId);
} 