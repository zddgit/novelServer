package com.free.novel.exception;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用来捕获程序运行过程中出现的异常,与@ControllerAdvice标注的类实现的功能差不多一样
 */
@Component
public class GlobalHandlerExceptionResolver extends AbstractHandlerExceptionResolver {

    public GlobalHandlerExceptionResolver() {
        //设置优先级
//        setOrder(Integer.MAX_VALUE);
        setOrder(0);
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        httpServletResponse.setContentType("application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code","500");
        jsonObject.put("message","服务器捕获异常信息："+e.getMessage());
        try {
            httpServletResponse.getOutputStream().write(jsonObject.toJSONString().getBytes("utf-8"));
            httpServletResponse.setStatus(500);
            httpServletResponse.getOutputStream().flush();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return new ModelAndView();
    }
}
