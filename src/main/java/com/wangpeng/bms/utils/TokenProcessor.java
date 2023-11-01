package com.wangpeng.bms.utils;
/**
 * 生成Token的工具类：  
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Base64;

public class TokenProcessor {

    private TokenProcessor(){};
    private static final TokenProcessor instance = new TokenProcessor();

    public static TokenProcessor getInstance() {
        return instance;
    }

    /**
     * 生成Token
     * @return
     */
    public String makeToken() {
        String token = (System.currentTimeMillis() + new Random().nextInt(999999999)) + "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5 =  md.digest(token.getBytes());
            String encodedToken = Base64.getEncoder().encodeToString(md5);
            return encodedToken;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
