package cn.yun.oddworld.controller;

import cn.yun.oddworld.dto.BaseResult;
import cn.yun.oddworld.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/init")
@RequiredArgsConstructor
public class InitController {

    private final FileService fileService;

    @PostMapping("/user")
    public ResponseEntity<BaseResult<Void>> initializeUser(@RequestParam("userId") Long userId) {
        fileService.initializeUser(userId);
        return ResponseEntity.ok(new BaseResult<>(200, "Initialization successful", null));
    }
} 