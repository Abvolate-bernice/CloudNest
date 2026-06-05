package com.bbj.lease.web.admin.controller.login;


import com.bbj.lease.common.result.Result;
import com.bbj.lease.web.admin.service.LoginService;
import com.bbj.lease.web.admin.vo.login.CaptchaVo;
import com.bbj.lease.web.admin.vo.login.LoginVo;
import com.bbj.lease.web.admin.vo.system.user.SystemUserInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "后台管理系统登录管理")
@RestController
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private LoginService service;

    static class LoginControllerService {

    }
    @Operation(summary = "获取图形验证码")
    @GetMapping("login/captcha")
    public Result<CaptchaVo> getCaptcha() {
        CaptchaVo captchaVo = service.getCaptcha();
//captchaVo 中有两个String类型的属性 key image(tobase64将图片转换为字符串)
//        admin:login:edb19b8d-a960-4f44-99a
//        admin:login:edb19b8d-a960-4f44-99a6-2ba71f3e9394
        return Result.ok(captchaVo);
    }

    @Operation(summary = "登录")
    @PostMapping("login")
//最后经过层层检查之后要返回一个令牌Token
    public Result<String> login(@RequestBody LoginVo loginVo) {
        String string = service.login(loginVo);


        return Result.ok(string);
    }

    @Operation(summary = "获取登陆用户个人信息")
    @GetMapping("info")
    public Result<SystemUserInfoVo> info() {
        return null;
    }
}