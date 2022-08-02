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
public class UserRegiesterRequest implements Serializable {


    private static final long serialVersionUID = -6373078211703403313L;


    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String planetCode;
}
