package com.free.novel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages = "com.free.novel.mapper")
public class NovelApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovelApplication.class, args);
    }
}
