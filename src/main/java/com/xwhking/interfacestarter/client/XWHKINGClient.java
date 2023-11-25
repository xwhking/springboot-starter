package com.xwhking.interfacestarter.client;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

import static com.xwhking.interfacestarter.utils.GenSign.genSign;

/**
 * 发起请求时候一定要注意，不要把secretKey直接传送，只需要进行一个签名传送就好了，然后后端通过同样的方式进行签名
 * 的生成，进行对比，验证身份。
 */
@Data
public class XWHKINGClient {
    private String accessKey;
    private String secretKey;
    private Long userId;
    public XWHKINGClient(String accessKey,String secretKey,Long userId){
        this.userId = userId;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }
    public String getName(String name){
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        HashMap<String,String> headerMap = new HashMap<>();
        headerMap.put("accessKey",accessKey);
        headerMap.put("userId",userId.toString());
        headerMap.put("sign", genSign(accessKey,secretKey,userId));
        headerMap.put("timestamp",Long.toString(new Date().getTime()));
        String result1= HttpRequest.get("http://localhost:8252/user/name")
                .addHeaders(headerMap)
                .form(paramMap).execute().body();
        System.out.println(result1);
        return result1;
    }
    public String GetUser(){
        HashMap<String,String> headerMap = new HashMap<>();
        headerMap.put("accessKey",accessKey);
        headerMap.put("userId",userId.toString());
        headerMap.put("sign", genSign(accessKey,secretKey,userId));
        headerMap.put("timestamp",Long.toString(new Date().getTime()));
        String result1= HttpRequest.get("http://localhost:8252/user/getOne")
                .addHeaders(headerMap)
                .execute().body();
        System.out.println(result1);
        return result1;
    }

    public static void main(String[] args) {
    new XWHKINGClient("xwhking","admin123",123123l).getName("XWHKING");
    }
}
