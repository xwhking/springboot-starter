# Starter

一个 Starter 开发的案例，通过这个 Start 开发以后，把 jar 包发布的 maven 仓库别人能够调用。能够通过依赖引入。



## 开发流程

通过 Idea 创建一个项目然后选择基本依赖

- Spring Boot DevTools
- Spring Configuration Processor
- Lombok
- Spring Web

等，还是看你项目需要。



创建好了以后就是创建一个用户调用的客户端 (例如） ：

```java
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
```



然后在创建一个客户端需要的配置类，不需要也可以,这里需要配置ak（accessKey），sk（secretKey）所以弄一个配置类，这样子的话通过后面的配置，在以后项目中调用，能够在配置文件中，把这些配置写入。注意其中的注解，其中`@ConfigurationProperties(prefix = "xwhking.client")`就是在配置中写的前缀。其他注解就是告诉Spring 要扫描这个类，以及当作配置类。

```java
package com.xwhking.interfacestarter.config;

import com.xwhking.interfacestarter.client.XWHKINGClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
@Configuration
@ConfigurationProperties(prefix = "xwhking.client")
@Data
@ComponentScan
public class XWHKINGClientConfig {
    private String accessKey;
    private String secretKey;
    private String userId;
    @Bean
    public XWHKINGClient xwhkingClient(){
        return new XWHKINGClient(accessKey,secretKey,Long.parseLong(userId));
    }
}

```



然后就是在 resource 文件夹中建立一个 META-INF 文件夹，然后建立一个文件 `spring.factories` 。目的只生成配置提示

文件里面的内容如下：

 ```java
 org.springframework.boot.autoconfigure.EnableAutoConfiguration = com.xwhking.interfacestarter.XWHKINGClientConfig
 ```

配置类的地址相应修改。



最后要删除 pom.xml 文件中的 build 部分

pom.xml 文件展示：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.10</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.xwhking</groupId>
    <artifactId>InterfaceStarter</artifactId>
    <version>0.0.1</version>
    <name>InterfaceStarter</name>
    <description>InterfaceStarter</description>
    <properties>
        <java.version>8</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>5.8.16</version>
        </dependency>
    </dependencies>
</project>

```



 # :end: 

 