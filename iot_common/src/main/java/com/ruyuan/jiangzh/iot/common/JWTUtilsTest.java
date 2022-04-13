package com.ruyuan.jiangzh.iot.common;

public class JWTUtilsTest {

    /*
        eyJhbGciOiJIUzUxMiJ9.
        eyJyYW5kb21LZXkiOiJrZnM4M2YiLCJzdWIiOiJhbGxlbiIsImV4cCI6MTY1MDQ2MzIxMiwiaWF0IjoxNjQ5ODU4NDEyfQ.
        -HmmCezc9G6EFueF3WMFoErL_dHy9RqGz_rGE4dNXQaiqTRyjVAZtf_A16q-mxM18fTh5DwYsUEyu6WVmG0gTA
     */

    public static void main(String[] args) {
        /*
            1和2对应的是JWT的生成，背景：用户输入了用户名和密码以后，申请JWT

            3,4,5对应的是JWT的验证，背景：申请设备时携带了JWT，我们需要验证JWT的有效性和获取信息
         */
        JWTUtils jwtUtils = new JWTUtils();
        String username = "allen";
        // 1、生成 randomKey
        String randomKey = jwtUtils.genRandomKey();
        System.out.println("randomKey = " + randomKey);
        // 2、按照用户名+randomKey生成JWT
        String jwt = jwtUtils.genJWT(username, randomKey);
        System.out.println("jwt = " + jwt);
        // 3、在JWT中获取用户名
        String uname = jwtUtils.getUserNameInToken(jwt);
        System.out.println("uname = " + uname);
        // 4、在JWT中获取randomKey
        String randomk = jwtUtils.parserRandomKey(jwt);
        System.out.println("randomk = " + randomk);
        // 5、判断JWT是否过期
        Boolean isTokenExpired  = jwtUtils.isTokenExpired(jwt);
        System.out.println("isTokenExpired = " + isTokenExpired);
    }

}
