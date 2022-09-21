package com.sipc.xxsc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.CrossOrigin;

@MapperScan("com.sipc.xxsc.mapper")
@SpringBootApplication
@CrossOrigin
@EnableTransactionManagement
@EnableCaching
@EnableScheduling
public class XxscApplication {
    public static void main(String[] args) {
        SpringApplication.run(XxscApplication.class, args);
    }

}
