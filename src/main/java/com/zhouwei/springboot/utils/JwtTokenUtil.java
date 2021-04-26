package com.zhouwei.springboot.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtil {
    //密钥
    public static final String SECRET = "zhouwei;6666";
    //过期时间:秒
    public static final int EXPIRE = 60*60;
    /**
     *author:  zhouwei
     *time:  2020/1/16
     *function:
     */
    public static String createToken(String userId,String userName){
        try{
            Calendar nowTime = Calendar.getInstance();
            nowTime.add(Calendar.SECOND, EXPIRE);
            Date expireDate = nowTime.getTime();

            Map<String, Object> map = new HashMap<>();
            map.put("alg", "HS256");
            map.put("typ", "JWT");
            String token = JWT.create()
                    .withHeader(map)//头
                    .withClaim("userId", userId)
                    .withClaim("userName",userName)
                    .withSubject("测试")//
                    .withIssuer("zhouwei") //暂时可以不需要
                    .withIssuedAt(new Date())//签名时间
                    .withExpiresAt(expireDate)//过期时间
                    .sign(Algorithm.HMAC256(SECRET));//签名
            return token;
        }catch (Exception e){

        }
        return "";
    }

   /**
    *author:  zhouwei
    *time:  2020/1/16
    *function: 验证Token
    */
    public static boolean verifyToken(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT jwt = null;
        try {
            jwt = verifier.verify(token);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     *author:  zhouwei
     *time:  2020/1/16
     *function:解析Token
     */
    public static Map<String, Claim> parseToken(String token){
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaims();
    }

    public static void main(String[] args) {
        //String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLmtYvor5UiLCJ1c2VyTmFtZSI6InhpYW9taW5nIiwiZXhwIjoxNTc5MTU0NzM5LCJ1c2VySWQiOiIxMjMiLCJpYXQiOjE1NzkxNTQ3MDl9.BD8NlPzfAd3YwFv-TzJDfnse7eoVWbp4mzbIi_8v_64\n";
       String token= createToken("周伟2020-05-13","xiaoming");
       System.out.println(token);
        try {
           System.out.println(verifyToken(token));
            DecodedJWT jwt = JWT.decode(token);
            System.out.println(parseToken(token).get("userId").asString());
            System.out.println(jwt.getClaim("userId").asString());
            System.out.println(jwt.getClaim("userName").asString());
            System.out.println(jwt.getSubject());
        }catch (Exception e){

        }
        System.out.println("====");
    }

}
