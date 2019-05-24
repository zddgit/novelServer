package com.free.novel.exception;

import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


/**
 * 用来处理系统没有映射的路径，返回的错误视图
 */
@Component
public class URIErrorViewResolver implements ErrorViewResolver, Ordered {


    @Override
    public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
        response.setContentType("application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",status.value());
        jsonObject.put("message","URI not Found!");
        try {
            response.getOutputStream().write(jsonObject.toJSONString().getBytes("utf-8"));
            response.setStatus(status.value());
            response.getOutputStream().flush();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return new ModelAndView();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
