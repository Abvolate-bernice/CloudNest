package com.bbj.lease.common.utils;

import com.bbj.lease.common.exception.LeaseException;
import com.bbj.lease.common.result.ResultCodeEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {


    private static SecretKey key = Keys.hmacShaKeyFor("ntL5zCoWMMBFcNKpEbtWXgMQbF11CNyi".getBytes());
    public static   String createToken(Long userId, String username) {


        //创建JWT令牌 Header(包含JWT自身的一些信息) payload（可以自定义保存要传递的信息 用户信息） signature  （由Header \payload\ key 通过签名算法计算得出的一个字符串，防止数据内容被篡改）
        String jwt = Jwts.builder()
                .setSubject("LOGIN_USER")
                .claim("userId", userId)
                .claim("username", username)
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key,SignatureAlgorithm.HS256)
                .compact();
        return jwt;
    }

    public static   Claims paraseToken(String token){
        if (token==null){
            throw new LeaseException(ResultCodeEnum.ADMIN_LOGIN_AUTH);  //没有Token说明还是没有登录
        }

        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)   //根据JWT的前两部分 加KEY 重新计算一下 是不是和JWT第三部分一样
                    .build() //得到解析器
                    .parseClaimsJws(token);   //会解析出来JWT
            System.out.println("内容"+claimsJws.getBody());
            return claimsJws.getBody();  // //会解析出来JWT 的第二部分Payload  ,使用claimsJws.getBody();就可以获得在Payload中设置的每个字段了
        }catch (ExpiredJwtException e) {
            throw new LeaseException(ResultCodeEnum.TOKEN_EXPIRED);
        }catch (JwtException e){
            throw new LeaseException(ResultCodeEnum.TOKEN_INVALID);

        }
    }
}

//private static SecretKey secretKey = Keys.hmacShaKeyFor("CY29Eb04RPNyQPxACH2jBNWFGn0ypMhc".getBytes());
//
//
//public static String createToken(Long userId, String username) {
//
//    return Jwts.builder()
//            .setSubject("LOGIN_USER")
//            .setExpiration(new Date(System.currentTimeMillis() + 3600000 * 24 * 365L))
//            .claim("userId", userId)
//            .claim("username", username)
//            .signWith(secretKey, SignatureAlgorithm.HS256)
//            .compact();
//}
//
//public static Claims parseToken(String token) {
//
//    if (token == null) {
//        throw new LeaseException(ResultCodeEnum.ADMIN_LOGIN_AUTH);
//    }
//
//    try {
//        Jws<Claims> claimsJws = Jwts.parserBuilder()
//                .setSigningKey(secretKey)
//                .build()
//                .parseClaimsJws(token);
//        return claimsJws.getBody();
//    } catch (ExpiredJwtException e) {
//        throw new LeaseException(ResultCodeEnum.TOKEN_EXPIRED);
//    } catch (JwtException e) {
//        throw new LeaseException(ResultCodeEnum.TOKEN_INVALID);
//    }
//}
//
//
//public static void main(String[] args) {
//    System.out.println(createToken(1L, "13888888888"));
