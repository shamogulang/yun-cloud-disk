package cn.yun.oddworld.repository.impl;

import cn.yun.oddworld.entity.FileInfo;
import cn.yun.oddworld.mapper.FileInfoMapper;
import cn.yun.oddworld.repository.FileInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class FileInfoRepositoryImpl implements FileInfoRepository {


    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Override
    public int insert(FileInfo fileInfo) {
        return fileInfoMapper.insert(fileInfo);
    }



    @Override
    public FileInfo selectByIdAndUserId(Long id, Long userId) {
        return fileInfoMapper.selectByIdAndUserId(id, userId);
    }

    @Override
    public List<FileInfo> selectByUserId(Long userId) {
        return fileInfoMapper.selectByUserId(userId);
    }

    @Override
    public int deleteByIdAndUserId(Long id, Long userId) {
        return fileInfoMapper.deleteByIdAndUserId(id, userId);
    }

    @Override
    public int completeByIdAndUserId(Long id, Long userId) {

        return fileInfoMapper.completeByIdAndUserId(id, userId);
    }

    @Override
    public List<FileInfo> selectByPathAndUserId(Long userId, String path) {

        return fileInfoMapper.selectByPathAndUserId(path, userId);
    }

    @Override
    public FileInfo selectByUserIdAndFileNameAndPath(Long userId, String fileName, String filePath) {
        return fileInfoMapper.selectByUserIdAndFileNameAndPath(userId, fileName, filePath);
    }

    @Override
    public List<FileInfo> selectByUserIdAndPathPrefix(Long userId, String prefix) {
        return fileInfoMapper.selectByUserIdAndPathPrefix(userId, prefix);
    }

    @Override
    public List<FileInfo> selectByUserIdAndParentId(Long userId, String parentId) {
        return fileInfoMapper.selectByUserIdAndParentId(userId, parentId);
    }

    @Override
    public int countNormalFiles(Long userId) {
        return fileInfoMapper.countNormalFiles(userId);
    }

    @Override
    public List<FileInfo> selectNormalFiles(Long userId) {
        return fileInfoMapper.selectNormalFiles(userId);
    }

    @Override
    public int countRecentNormalFiles(Long userId, java.time.LocalDateTime fromTime) {
        return fileInfoMapper.countRecentNormalFiles(userId, fromTime);
    }

    @Override
    public List<FileInfo> selectRecentFiles(Long userId, java.time.LocalDateTime fromTime) {
        return fileInfoMapper.selectRecentFiles(userId, fromTime);
    }
}
