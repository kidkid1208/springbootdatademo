package com.lzh.handler;

import org.apache.ibatis.annotations.Result;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Set;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handle(ValidationException exception) {
        String exceptions="";
        if(exception instanceof ConstraintViolationException) {
            ConstraintViolationException exs = (ConstraintViolationException) exception;

            Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
            for (ConstraintViolation<?> item : violations) {
                //打印验证不通过的信息
                System.out.println(item.getMessage());
                exceptions += "\n" + item.getMessage();
            }
            return exceptions;
        }
        return "bad request";
    }
}
