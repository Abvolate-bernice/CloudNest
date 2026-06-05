package com.bbj.lease.web.admin.service;

import com.bbj.lease.web.admin.vo.login.CaptchaVo;
import com.bbj.lease.web.admin.vo.login.LoginVo;
import com.bbj.lease.web.admin.vo.system.user.SystemUserInfoVo;

public interface LoginService {

    CaptchaVo getCaptcha();


    String login(LoginVo loginVo);

    SystemUserInfoVo getUserInfoById(Long userId);
}
