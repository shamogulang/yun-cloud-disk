package cn.yun.oddworld.controller;

import cn.yun.oddworld.dto.BaseResult;
import cn.yun.oddworld.dto.FileCompleteRequest;
import cn.yun.oddworld.dto.FileUploadRequest;
import cn.yun.oddworld.entity.FileInfo;
import cn.yun.oddworld.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/prepare")
    public ResponseEntity<BaseResult<FileInfo>> prepareFile(@RequestAttribute("userId") Long userId,
            @RequestBody FileUploadRequest uploadRequest) {

        uploadRequest.setUserId(userId);
        uploadRequest.setStatus(1);
        FileInfo fileInfo = fileService.prepareFile(uploadRequest);
        return ResponseEntity.ok(new BaseResult<>(200, null, fileInfo));
    }

    @PostMapping("/complete")
    public ResponseEntity<BaseResult<FileInfo>> completeFile(@RequestAttribute("userId") Long userId,
            @RequestBody FileCompleteRequest fileCompleteRequest) {
        fileCompleteRequest.setUserId(userId);
        fileService.completeFile(fileCompleteRequest);
        return ResponseEntity.ok(new BaseResult<>(200, null, null));
    }

    @PostMapping("/mkdir")
    public ResponseEntity<BaseResult<FileInfo>> completeFolder(@RequestAttribute("userId") Long userId,@RequestBody FileUploadRequest uploadRequest) {

        uploadRequest.setFileType("directory");
        uploadRequest.setUserId(userId);
        uploadRequest.setStatus(2);
        uploadRequest.setFileSize(0L);
        uploadRequest.setFileHash("");
        FileInfo fileInfo = fileService.prepareFile(uploadRequest);
        return ResponseEntity.ok(new BaseResult<>(200, null, fileInfo));
    }


    @DeleteMapping("/{fileId}")
    public ResponseEntity<BaseResult<Object>> deleteFile(
            @PathVariable Long fileId,
            @RequestAttribute("userId") Long userId) {
        fileService.deleteFile(fileId, userId);
        return ResponseEntity.ok((new BaseResult<>(200, null)));
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<FileInfo> getFileInfo(
            @PathVariable Long fileId,
            @RequestAttribute("userId") Long userId) {
        return ResponseEntity.ok(fileService.getFileInfo(fileId, userId));
    }

    @GetMapping("/list")
    public ResponseEntity<BaseResult<List<FileInfo>>> listFiles(@RequestAttribute("userId") Long userId,
            @RequestParam(required = true) String path) {
        List<FileInfo> fileInfos = fileService.listFiles(userId, path);
        return ResponseEntity.ok(new BaseResult<>(200, null, fileInfos));
    }

    @GetMapping("/{fileId}/url")
    public ResponseEntity<String> getFileUrl(
            @PathVariable Long fileId,
            @RequestAttribute("userId") Long userId) {
        return ResponseEntity.ok(fileService.getFileUrl(fileId, userId));
    }

    @GetMapping("/count")
    public ResponseEntity<BaseResult<Integer>> countNormalFiles(@RequestAttribute("userId") Long userId) {
        int count = fileService.countNormalFiles(userId);
        return ResponseEntity.ok(new BaseResult<>(200, null, count));
    }

    @GetMapping("/recent/count")
    public ResponseEntity<BaseResult<Integer>> countRecentNormalFiles(@RequestAttribute("userId") Long userId) {
        LocalDateTime fromTime = LocalDateTime.now().minusDays(1);
        int count = fileService.countRecentNormalFiles(userId, fromTime);
        return ResponseEntity.ok(new BaseResult<>(200, null, count));
    }

    @GetMapping("/recent/list")
    public ResponseEntity<BaseResult<List<FileInfo>>> getRecentFiles(@RequestAttribute("userId") Long userId) {
        LocalDateTime fromTime = LocalDateTime.now().minusMonths(1);
        List<FileInfo> files = fileService.getRecentFiles(userId, fromTime);
        return ResponseEntity.ok(new BaseResult<>(200, null, files));
    }
} 