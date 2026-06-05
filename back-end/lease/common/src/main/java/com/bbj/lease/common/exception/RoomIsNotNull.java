package com.bbj.lease.common.exception;

import com.bbj.lease.common.result.ResultCodeEnum;
import lombok.Data;

@Data
public class RoomIsNotNull extends RuntimeException{
    private Integer code;

    //提供构造方法 为后续new 异常对象做准备
    public  RoomIsNotNull (Integer code,String  message){
        super(message);
        this.code=code;
    }

    public  RoomIsNotNull (ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();

    }
}
