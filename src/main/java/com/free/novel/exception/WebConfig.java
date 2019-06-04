package com.free.novel.exception;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    //根据条件来判断是否加载此类到spring容器
    @Bean
    @ConditionalOnWebApplication
    public GlobalHandlerExceptionResolver globalhandlerexceptionresolver(){
        return  new GlobalHandlerExceptionResolver();
    }

}
