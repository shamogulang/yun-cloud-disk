package cn.yun.oddworld.mapper;

import cn.yun.oddworld.entity.StorageBucket;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface StorageBucketMapper {
    @Insert("INSERT INTO storage_bucket (name, provider, bucket_name, region, endpoint, access_key, secret_key, is_default, created_at) VALUES (#{name}, #{provider}, #{bucketName}, #{region}, #{endpoint}, #{accessKey}, #{secretKey}, #{isDefault}, #{createdAt})")
    int insert(StorageBucket storageBucket);

    @Update("UPDATE storage_bucket SET name=#{name}, provider=#{provider}, bucket_name=#{bucketName}, region=#{region}, endpoint=#{endpoint}, access_key=#{accessKey}, secret_key=#{secretKey}, is_default=#{isDefault} WHERE id=#{id}")
    int update(StorageBucket storageBucket);

    @Delete("DELETE FROM storage_bucket WHERE id=#{id}")
    int deleteById(@Param("id") Long id);

    @Select("SELECT * FROM storage_bucket WHERE id=#{id}")
    StorageBucket selectById(@Param("id") Long id);

    @Select("SELECT * FROM storage_bucket WHERE name=#{name}")
    StorageBucket selectByName(@Param("name") String name);

    @Select("SELECT * FROM storage_bucket")
    List<StorageBucket> selectAll();

    @Select("SELECT * FROM storage_bucket WHERE status=1")
    List<StorageBucket> selectActiveBuckets();
} 