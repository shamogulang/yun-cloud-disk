package cn.yun.oddworld.feign;

import cn.yun.oddworld.dto.AuthRequest;
import cn.yun.oddworld.dto.BaseResult;
import cn.yun.oddworld.dto.UserAuthData;
import cn.yun.oddworld.feign.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "${user.auth:''}", configuration = FeignConfiguration.class)
public interface UserFeign {


    @PostMapping("/user/auth")
    BaseResult<UserAuthData> auth(@RequestBody AuthRequest authRequest);
}