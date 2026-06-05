package com.bbj.lease.common.exception;

import com.bbj.lease.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handle(Exception e) {
        e.printStackTrace();
        return Result.fail();
    }


    //    @ExceptionHandler(LeaseException.class)
//    public Result handle(LeaseException e) {
//        String message = e.getMessage();
//        Integer code = e.getCode();
//        e.printStackTrace();
//        return Result.fail(code, message);
//    }
    @ExceptionHandler(RoomIsNotNull.class)
    @ResponseBody
    public Result handle(RoomIsNotNull e) {
        e.printStackTrace();
        Integer  code = e.getCode();
        String message = e.getMessage();

        return Result.fail(code,message);
    }
}
