package com.harvey.system.exception.handler;

import com.harvey.system.base.RespResult;
import com.harvey.system.enums.ErrorCodeEnum;
import com.harvey.system.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Harvey
 * @date 2024-11-06 22:09
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public RespResult<String> handleException(Throwable e) {
        log.error(e.getMessage(), e);
        return RespResult.fail(e.getMessage());
    }

    /**
     * 参数缺少或错误
     * @param e
     * @return
     */
    @ExceptionHandler({TypeMismatchException.class, BindException.class})
    public RespResult<?> paramExceptionHandler(Exception e) {
        if (e instanceof BindException) {
            if (e instanceof MethodArgumentNotValidException) {
                List<FieldError> fieldErrors = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors();
                List<String> msgList = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
                return RespResult.error(String.join(",", msgList));
            }

            List<FieldError> fieldErrors = ((BindException) e).getFieldErrors();
            List<String> error = fieldErrors.stream().map(field -> field.getField() + ":" + field.getRejectedValue()).toList();
            String errorMsg = ErrorCodeEnum.PARAM_ERROR.getMsg() + ":" + error;
            return RespResult.error(errorMsg);
        }
        return RespResult.error();
    }

    @ExceptionHandler(BusinessException.class)
    public RespResult<String> handleServiceException(BusinessException e) {
        log.error(e.getMessage(), e);
        return RespResult.fail(e.getCode(), e.getMessage());
    }
}
