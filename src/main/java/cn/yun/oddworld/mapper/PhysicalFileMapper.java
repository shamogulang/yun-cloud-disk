package cn.yun.oddworld.mapper;

import cn.yun.oddworld.entity.PhysicalFile;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface PhysicalFileMapper {
    @Insert("INSERT INTO physical_file (file_hash, file_size, file_type, s3_path, reference_count, storage_id, created_at) VALUES (#{fileHash}, #{fileSize}, #{fileType}, #{s3Path}, #{referenceCount}, #{storageId}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PhysicalFile physicalFile);

    @Update("UPDATE physical_file SET file_hash=#{fileHash}, file_size=#{fileSize}, file_type=#{fileType}, s3_path=#{s3Path}, reference_count=#{referenceCount}, storage_id=#{storageId} WHERE id=#{id}")
    int update(PhysicalFile physicalFile);

    @Delete("DELETE FROM physical_file WHERE id=#{id}")
    int deleteById(@Param("id") Long id);

    @Select("SELECT * FROM physical_file WHERE id=#{id}")
    PhysicalFile selectById(@Param("id") Long id);

    @Select("SELECT * FROM physical_file WHERE file_hash=#{fileHash}")
    PhysicalFile selectByHash(@Param("fileHash") String fileHash);

    @Select("SELECT * FROM physical_file")
    List<PhysicalFile> selectAll();
} 