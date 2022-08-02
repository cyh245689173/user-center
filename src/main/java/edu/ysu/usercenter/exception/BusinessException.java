package edu.ysu.usercenter.exception;

import edu.ysu.usercenter.common.ErrorCode;

/**
 * 自定义业务异常类
 *
 * @auther xiaochen
 * @create 2022-07-26 9:03
 */
public class BusinessException extends RuntimeException {
    private final int code;
    private final String description;


    //多写几个构造函数，方便我们灵活的传参
    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }


    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}