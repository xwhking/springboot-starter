package com.xwhking.interfacestarter.utils;

import cn.hutool.crypto.SecureUtil;
import lombok.Data;


@Data
public class GenSign {
    public static String genSign(String accessKey,String secretKey,Long userId){
        String key = "xwhking" + "." + accessKey + "." + secretKey + "."  + userId;
        return SecureUtil.sha256(key);
    }
}
