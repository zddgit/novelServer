package com.free.novel.exception;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 专门用来捕获在Controller层出现的异常与实现AbstractHandlerExceptionResolver的类差不多一样
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    public Object defaultErrorHandler(HttpServletRequest req, HttpServletResponse response, Exception e) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code","500");
        jsonObject.put("message","GlobalExceptionHandler："+e.getMessage());
        return jsonObject;
    }
}
