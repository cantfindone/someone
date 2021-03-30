package com.superjoy.someone.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理
 * @author Ping
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 通用异常处理
     */
    @ExceptionHandler(CommonException.class)
    public Map<String, Object> handlerOtherException(CommonException ex, HttpServletResponse response){
        Map<String, Object> result = new HashMap<>(3);
        result.put("message", ex.getMessage());
        response.setStatus(ex.getCode());
        log.error(ex.getMessage());
        return result;
    }

    /**
     * 参数校验异常格式化
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handlerValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> result = new HashMap<>(3);
        String field = ex.getBindingResult().getFieldError().getField();
        result.put("message", "参数'" + field + "'" + ex.getBindingResult().getFieldError().getDefaultMessage());
        return result;
    }

    /**
     * 其余异常处理
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handlerException(Exception ex){
        Map<String, Object> result = new HashMap<>(3);
        result.put("message", "服务器异常，请联系管理员");
        ex.printStackTrace();
        return result;
    }
}
