package com.ruyuan.jiangzh.iot.common;

import com.google.common.collect.Maps;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;
import java.util.Random;

public class JWTUtils {

    // secretKey名称
    private String secretKey = "randomKey";
    // token过期时间  7 * 24 * 60 * 60;
    private Long expriation = 604800L;
    // 混淆字符串
    private String secretStr = "defaultSecret";

    private static String jwtHeaderName = "jwt";
    /**
     *  生成 randomKey
     * @return
     */
    public String genRandomKey(){
        // 生成一个6位的随机字符串
        return getRandomString(6);
    }

    private String getRandomString(int length){
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<length; i++){
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }

        return sb.toString();
    }

    /*
        按照用户名+randomKey生成JWT
     */
    public String genJWT(String username, String randomKey){
        Map<String, Object> claims = Maps.newHashMap();
        claims.put(secretKey, randomKey);

        return jwtImpl(claims, username);
    }

    /*
           JWT的实现
     */
    private String jwtImpl(Map<String, Object> claims,String subject){
        Date beginDate = new Date();
        Date expirationDate = new Date(beginDate.getTime() + expriation * 1000);

        String result = Jwts.builder()
                .setClaims(claims)
                // 一般是指人，username
                .setSubject(subject)
                // 生成时间
                .setIssuedAt(beginDate)
                // 过期时间
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secretStr)
                .compact();

        return result;
    }

    // 在JWT中获取用户名
    public String getUserNameInToken(String token){
        return getClaimFromToken(token).getSubject();
    }

    // 在JWT中获取randomKey
    public String parserRandomKey(String token){
        Claims claimFromToken = getClaimFromToken(token);
        Object value = claimFromToken.get(secretKey);
        return value.toString();
    }

    // 判断JWT是否过期
    public Boolean isTokenExpired(String token){
        Date expirationDate = getClaimFromToken(token).getExpiration();
        return expirationDate.before(new Date());
    }

    private Claims getClaimFromToken(String token){
        Claims result = Jwts.parser()
                .setSigningKey(secretStr)
                .parseClaimsJws(token)
                .getBody();

        return result;
    }

    public static String getJwtHeaderName() {
        return jwtHeaderName;
    }
}
