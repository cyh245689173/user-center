package edu.ysu.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @auther xiaochen
 * @create 2022-07-25 19:23
 */
@Data
public class BaseResponse<T> implements Serializable {
    private int code;

    //因为做的是一个通用的返回类，所以不管接口返回的是什么类型都能使用，所以加一个泛型
    //提高了可重用性
    private T data;

    private String message;

    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "","");
    }

    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(),null,errorCode.getMessage(),errorCode.getDescription());
    }
}
