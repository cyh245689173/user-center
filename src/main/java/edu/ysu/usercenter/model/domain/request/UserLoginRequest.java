package edu.ysu.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @auther xiaochen
 * @create 2022-06-02 16:28
 */

@Data
public class UserLoginRequest implements Serializable {


    private static final long serialVersionUID = -1614233668711606753L;
    private String userAccount;
    private String userPassword;

}
