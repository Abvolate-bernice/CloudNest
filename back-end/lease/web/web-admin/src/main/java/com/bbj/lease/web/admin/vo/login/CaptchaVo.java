package com.bbj.lease.web.admin.vo.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(description = "图像验证码")
@AllArgsConstructor
public class CaptchaVo {

    @Schema(description="验证码图片信息")
    // 这里图片的类型是String类型   主要是使用base64算法将任意二进制内容转换为字符串
    private String image;

    @Schema(description="验证码key")
    private String key;
}
