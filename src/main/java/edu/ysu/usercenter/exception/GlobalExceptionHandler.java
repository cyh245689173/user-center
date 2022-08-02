package edu.ysu.usercenter.exception;

import edu.ysu.usercenter.common.BaseResponse;
import edu.ysu.usercenter.common.ErrorCode;
import edu.ysu.usercenter.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * @auther xiaochen
 * @create 2022-07-26 10:15
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e){
        log.error("businessException:"+e.getMessage(),e);
        return ResultUtils.error(e.getCode(),e.getMessage(),e.getDescription());
    }


    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(BusinessException e){
        log.error("runtimeException:",e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),"");
    }

}
