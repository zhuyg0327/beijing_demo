package com.rent.Base;

import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * token加密
 * author zyg  2020年8月30日
 */
@Component
public class Md5TokenGenerator implements TokenGenerator{
    @Override
    public String generate(String... strings) {
        long timestamp = System.currentTimeMillis();
        String tokenMeta = "";
        for (String s : strings) {
            tokenMeta = tokenMeta + s;
        }
        tokenMeta = tokenMeta + timestamp;
        String token = DigestUtils.md5DigestAsHex(tokenMeta.getBytes());
        return token;
    }
}
