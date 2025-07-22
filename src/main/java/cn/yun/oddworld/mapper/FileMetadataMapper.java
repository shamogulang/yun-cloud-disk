package cn.yun.oddworld.mapper;

import cn.yun.oddworld.entity.FileMetadata;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FileMetadataMapper {
    @Insert("INSERT INTO file_metadata (physical_file_id, camera_taken_at, latitude, longitude, video_duration, metadata_json, created_at) VALUES (#{physicalFileId}, #{cameraTakenAt}, #{latitude}, #{longitude}, #{videoDuration}, #{metadataJson}, #{createdAt})")
    int insert(FileMetadata fileMetadata);

    @Update("UPDATE file_metadata SET physical_file_id=#{physicalFileId}, camera_taken_at=#{cameraTakenAt}, latitude=#{latitude}, longitude=#{longitude}, video_duration=#{videoDuration}, metadata_json=#{metadataJson} WHERE id=#{id}")
    int update(FileMetadata fileMetadata);

    @Delete("DELETE FROM file_metadata WHERE id=#{id}")
    int deleteById(@Param("id") Long id);

    @Select("SELECT * FROM file_metadata WHERE id=#{id}")
    FileMetadata selectById(@Param("id") Long id);

    @Select("SELECT * FROM file_metadata WHERE physical_file_id=#{physicalFileId}")
    FileMetadata selectByPhysicalFileId(@Param("physicalFileId") Long physicalFileId);
} 