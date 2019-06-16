package com.free.novel.exception;

import com.free.novel.entity.BaseResponse;
import com.free.novel.entity.CustomizeException;
import com.free.novel.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 用来捕获程序运行过程中出现的异常,与@ControllerAdvice标注的类实现的功能差不多一样
 */
public class GlobalHandlerExceptionResolver extends AbstractHandlerExceptionResolver {

    private Logger logger = LoggerFactory.getLogger(GlobalHandlerExceptionResolver.class);

    public GlobalHandlerExceptionResolver() {
        //设置优先级
//        setOrder(Integer.MAX_VALUE);
        setOrder(0);
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        e.printStackTrace();
        OutputStream outputStream=null;
        try {
            outputStream = httpServletResponse.getOutputStream();
            httpServletResponse.setContentType("application/json");
            BaseResponse baseResponse;
            if(e instanceof CustomizeException){
                CustomizeException ec = (CustomizeException) e;
                baseResponse = new BaseResponse(ec.getCode(),ec.getMessage());
            }else {
                baseResponse = new BaseResponse( httpServletResponse.getStatus(),e.getMessage()==null?"NullPointException":e.getMessage());
            }
            outputStream.write(JsonUtil.obj2Json(baseResponse).getBytes(Charset.forName("utf-8")));
            outputStream.flush();
        } catch (IOException e1) {
            e.printStackTrace();
        }finally {
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e1) {
                    e.printStackTrace();
                }
            }
        }

        return new ModelAndView();
    }
}
