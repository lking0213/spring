package com.xahi.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;

/**
 * jwt工具类
 *
 * Created by liwq on 2019/6/21 15:08
 */
@Slf4j
public class JwtUtil {
    private JwtUtil() {
    }

    //密钥
    private static final String SECRETKEY = "xahi";
    //过期时间
    private static final long EXPIRATION_MILLS = 60*60*1000L;

    /**
     * 生成token
     *
     * @param claims
     *                  token中包含的Payload
     * @return
     */
    public static String doGenerateToken(Map<String, Object> claims) {
        return doGenerateToken(claims, SECRETKEY, EXPIRATION_MILLS);
    }

    /**
     * 生成token
     *
     * @param claims
     *                      token中包含的Payload
     * @param secretKey
     *                      密钥
     * @param expirationMills
     *                      过期时间毫秒数
     * @return
     */
    public static String doGenerateToken(Map<String, Object> claims, String secretKey, long expirationMills) {
        Date expirationDate = calculateExpirationDate(expirationMills);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * 验证token是否合法
     *
     * @param token
     *                  令牌
     * @return
     */
    public static Boolean validateToken(String token) {
        return validateToken(token, SECRETKEY);
    }

    /**
     * 验证token是否合法
     *
     * @param token
     *                  令牌
     * @param secretKey
     *                  密钥
     * @return
     */
    public static Boolean validateToken(String token, String secretKey) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token.trim());
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 获取token中的Payload信息
     *
     * @param token
     *                  令牌
     * @return
     */
    public static Map<String, Object> getClaims(String token) {
        return getClaims(token, SECRETKEY);
    }

    /**
     * 获取token中的Payload信息
     *
     * @param token
     *                  令牌
     * @param secretKey
     *                  密钥
     * @return
     */
    public static Map<String, Object> getClaims(String token, String secretKey) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token.trim())
                .getBody();
    }

    /**
     * 刷新token
     *
     * @param token
     *                  令牌
     * @return
     */
    public static String refreshToken(String token){
        return doGenerateToken(getClaims(token));
    }

    /**
     * 刷新token
     *
     * @param token
     *                      令牌
     * @param secretKey
     *                      密钥
     * @param expirationMills
     *                      过期毫秒数
     *
     * @return
     */
    public static String refreshToken(String token, String secretKey, long expirationMills){
        return doGenerateToken(getClaims(token, secretKey), secretKey, expirationMills);
    }

    private static Date calculateExpirationDate(long expirationMills) {
        return new Date(System.currentTimeMillis() + expirationMills);
    }

//    public static void main(String[] args) throws InterruptedException {
//        Map<String, Object> map = new HashMap<>();
//        map.put("uid", "1");
//        map.put("username", "james");
//        String token = doGenerateToken(map, "hxhz", EXPIRATION_MILLS);
//        System.out.println(token);
////        Thread.sleep(4000);
////
////        String token1 = refreshToken(token);
////        System.out.println(token1);
////
////        getClaims(token).forEach((k, v) -> {
////            System.out.println("k: " + k + "; v:" + v);
////        });
////
////        Thread.sleep(3000);
////        System.out.println(validateToken("eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6Imh4emgiLCJleHAiOjE1NjExMDcxNTQsInVzZXJJZCI6IjEifQ.MBJJVjuzyt3DqRGgkW-qnky1YfYIbv0dKZz8ffOVTj8"));
//    }
}
