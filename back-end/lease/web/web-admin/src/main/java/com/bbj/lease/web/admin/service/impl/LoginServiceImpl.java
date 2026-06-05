package com.bbj.lease.web.admin.service.impl;

import com.bbj.lease.common.constant.RedisConstant;
import com.bbj.lease.common.exception.LeaseException;
import com.bbj.lease.common.result.ResultCodeEnum;
import com.bbj.lease.common.utils.JwtUtil;
import com.bbj.lease.model.entity.SystemUser;
import com.bbj.lease.model.enums.BaseStatus;
import com.bbj.lease.web.admin.mapper.SystemUserMapper;
import com.bbj.lease.web.admin.service.LoginService;
import com.bbj.lease.web.admin.vo.login.CaptchaVo;
import com.bbj.lease.web.admin.vo.login.LoginVo;
import com.bbj.lease.web.admin.vo.system.user.SystemUserInfoVo;
import com.wf.captcha.SpecCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SystemUserMapper systemUserMapper;


    @Override
    public CaptchaVo getCaptcha() {
        //设置图形验证码的参数：长，宽，验证码位数
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
        //生成二进制验证码图片，并将其代表的验证码转换为小写  大小写不敏感，后面用户传过来的验证码也要同样转为小写
        String text = specCaptcha.text().toLowerCase();
        String key = RedisConstant.ADMIN_LOGIN_PREFIX + UUID.randomUUID();

        //储存在redis中
        // key : admin:login:edb19b8d-a960-4f44-99a
        // value :2pnb
        stringRedisTemplate.opsForValue().set(key, text, 60, TimeUnit.SECONDS);
//specCaptcha.toBase64()会将生成的二进制验证码图片编码为字符串
//        System.out.println("text"+text);//图片中的字符内容text2pnb
        return new CaptchaVo(specCaptcha.toBase64(), key);
    }

    @Override
    public String login(LoginVo loginVo) {
//        前端发送username、password、captchaKey、captchaCode请求登录。
//        判断captchaCode是否为空，若为空，则直接响应验证码为空；若不为空进行下一步判断。
        if(loginVo.getCaptchaCode()==null){
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_EXPIRED);
        }
//        根据captchaKey从Redis中查询之前保存的code，若查询出来的code为空，则直接响应验证码已过期；若不为空进行下一步判断。
        String key = loginVo.getCaptchaKey();
        String code = stringRedisTemplate.opsForValue().get(key);
        if (code==null){
            throw new LeaseException(ResultCodeEnum.APP_LOGIN_CODE_EXPIRED);
        }
//        比较captchaCode和code，若不相同，则直接响应验证码不正确；若相同则进行下一步判断。
        if (!loginVo.getCaptchaCode().toLowerCase().equals(code)){
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_ERROR);
        }
//        根据username查询数据库，若查询结果为空，则直接响应账号不存在；若不为空则进行下一步判断。
        String username = loginVo.getUsername();
        //这里必须自己写SQL查询语句，通用service和Mapper会忽略password字段  select = false
        SystemUser systemUser = systemUserMapper.selectOneByUsername(username);
        if (systemUser==null){
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_NOT_EXIST_ERROR);
        }
//        查看用户状态，判断是否被禁用，若禁用，则直接响应账号被禁；若未被禁用，则进行下一步判断。

                if(systemUser.getStatus().equals(BaseStatus.DISABLE)){
                    throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_DISABLED_ERROR);
                }
//        比对password和数据库中查询的密码，若不一致，则直接响应账号或密码错误，若一致则进行入最后一步。
        String md5Hex = DigestUtils.md5Hex(loginVo.getPassword());
                if(!systemUser.getPassword().equals(md5Hex)){
                    throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_ERROR);
                }
        //        创建JWT，并响应给浏览器。
        String token = JwtUtil.createToken(systemUser.getId(), systemUser.getUsername());


        return token;
    }

    @Override
    public SystemUserInfoVo getUserInfoById(Long userId) {
        return null;
    }
}
//@Override
//public CaptchaVo getCaptcha() {
//
//    SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
//    String key = RedisConstant.ADMIN_LOGIN_PREFIX + UUID.randomUUID();
//    String value = specCaptcha.text().toLowerCase();
//    stringRedisTemplate.opsForValue().set(key, value, RedisConstant.ADMIN_LOGIN_CAPTCHA_TTL_SEC, TimeUnit.SECONDS);
//    return new CaptchaVo(specCaptcha.toBase64(), key);
//}
//
//@Override
//public String login(LoginVo loginVo) {
//
//    if (!StringUtils.hasLength(loginVo.getCaptchaCode())) {
//        throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_NOT_FOUND);
//    }
//
//    String code = stringRedisTemplate.opsForValue().get(loginVo.getCaptchaKey());
//    if (!StringUtils.hasLength(code)) {
//        throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_EXPIRED);
//    }
//
//    if (!code.equals(loginVo.getCaptchaCode().toLowerCase())) {
//        throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_ERROR);
//    }
//
//    SystemUser systemUser = systemUserMapper.selectOneByUsername(loginVo.getUsername());
//
//    if (systemUser == null) {
//        throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_NOT_EXIST_ERROR);
//    }
//
//    if (systemUser.getStatus() == BaseStatus.DISABLE) {
//        throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_DISABLED_ERROR);
//    }
//
//    if (!systemUser.getPassword().equals(DigestUtils.md5Hex(loginVo.getPassword()))) {
//        throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_ERROR);
//    }
//
//    //创建JWT
//    return JwtUtil.createToken(systemUser.getId(), systemUser.getUsername());
//}
//
//@Override
//public SystemUserInfoVo getUserInfoById(Long userId) {
//
//    SystemUser systemUser = systemUserMapper.selectById(userId);
//    SystemUserInfoVo systemUserInfoVo = new SystemUserInfoVo();
//    systemUserInfoVo.setName(systemUser.getName());
//    systemUserInfoVo.setAvatarUrl(systemUser.getAvatarUrl());
//    return systemUserInfoVo;
//}
