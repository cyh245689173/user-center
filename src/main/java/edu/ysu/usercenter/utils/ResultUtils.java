package edu.ysu.usercenter.utils;

import edu.ysu.usercenter.common.BaseResponse;
import edu.ysu.usercenter.common.ErrorCode;

/**
 * 返回通用结果工具类
 *
 * @auther xiaochen
 * @create 2022-07-25 19:43
 */
public class ResultUtils {
    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }




    /**
     * 失败
     * @param code
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse error(int code, String message, String description) {
        return new BaseResponse<>(code,null, message, description);
    }

    /**
     * 失败
     * @param errorCode
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode, String message, String description) {
        return new BaseResponse<>(errorCode.getCode(), null, message, description);
    }


    /**
     *
     * @param errorCode
     * @param description
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode,String description) {
        return new BaseResponse<>(errorCode.getCode(), errorCode.getMessage(), description);
    }
}
