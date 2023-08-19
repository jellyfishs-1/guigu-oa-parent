package com.atguigu;

/**
 * @author jellyfish
 * @create 2023 -- 07 -- 25 -- 21:17
 * @描述
 * @email:1137648153@qq.com
 */


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

/**
 * ClassName: ServiceAuthApplication
 * Package: com.jerry.auth
 * Description:
 *
 * @Author jerry_jy
 * @Create 2023-02-28 22:03
 * @Version 1.0
 */
//@ComponentScan("com.atguigu.process")
@SpringBootApplication
public class ServiceAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceAuthApplication.class, args);

    }
}

