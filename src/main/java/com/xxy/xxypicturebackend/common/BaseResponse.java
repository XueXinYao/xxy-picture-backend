package com.xxy.xxypicturebackend.common;

import com.xxy.xxypicturebackend.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponse<T>  implements Serializable {
    private int code;
    private  T Data;
    private String message;

    public BaseResponse(int code, T data, String message){
        this.code = code;
        this.Data = data;
        this.message = message;
    }
    public BaseResponse(int code, T data){
        this(code,data,"");
    }
    public BaseResponse(int code, String message){
        this.code = code;
        this.message = message;
    }
    public BaseResponse(ErrorCode code){
        this(code.getCode(),code.getMessage());
    }
}
