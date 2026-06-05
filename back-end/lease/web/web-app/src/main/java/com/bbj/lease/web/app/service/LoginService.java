package com.bbj.lease.web.app.service;

import com.bbj.lease.web.app.vo.user.LoginVo;
import com.bbj.lease.web.app.vo.user.UserInfoVo;

public interface LoginService {
    void sendCode(String phone);

    String login(LoginVo loginVo);

    UserInfoVo getLoginUserInfoById(Long userId);
}
