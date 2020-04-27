package com.angel.product.handler;

import com.angel.common.exception.BusinessCode;
import com.angel.common.utils.R;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;

@RestControllerAdvice(basePackages = "com.angel.product.controller")
public class MyExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handlerValidateException(MethodArgumentNotValidException e){

        BindingResult result = e.getBindingResult();
            HashMap<String, String> map = new HashMap<>();
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                map.put(fieldError.getField(),fieldError.getDefaultMessage());
            }

            return R.error(BusinessCode.VALIDATE_FAILED.getCode(),BusinessCode.VALIDATE_FAILED.getErrorMsg()).put("errorMsg",map);
    }

    @ExceptionHandler(Exception.class)
    public R handlerException(Exception e){
        e.printStackTrace();
        return R.error(BusinessCode.OTHER_EXCEPTION.getCode(),BusinessCode.OTHER_EXCEPTION.getErrorMsg()).put("exception",e.getMessage());
    }

}
