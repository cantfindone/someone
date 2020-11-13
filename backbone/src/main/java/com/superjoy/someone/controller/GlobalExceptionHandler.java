package com.superjoy.someone.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ping
 */
@ControllerAdvice
@ResponseBody
@Log4j2
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handlerValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> result = new HashMap<>(3);
        String field = ex.getBindingResult().getFieldError().getField();
        result.put("message", "参数'" + field + "'" + ex.getBindingResult().getFieldError().getDefaultMessage());
        result.put("cause", "参数'" + field + "'错误");
        return result;
    }

    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handlerValidationException(org.springframework.web.servlet.NoHandlerFoundException ex) {
        Map<String, Object> result = new HashMap<>(3);
        result.put("message", ex.getMessage());
        result.put("cause","请求的资源不存在");
        return result;
    }


    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> defaultErrorHandler(HttpServletRequest request, Exception ex) throws Exception {
        log.error("Exception:", ex);
        Map<String, Object> result = new HashMap<>(3);
        result.put("message", ex.getMessage());
        result.put("cause", ex.getCause());
        return result;

    }

//    @ExceptionHandler(RuntimeException.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public Map<String, Object> handlerMyRuntimeException(RuntimeException ex) {
//        Map<String, Object> result = new HashMap<>(2);
//        result.put("message", ex.getMessage());
//        result.put("errorType", ex.getClass());
//        return result;
//    }
}
