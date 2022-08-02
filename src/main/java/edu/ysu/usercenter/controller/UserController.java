package edu.ysu.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.ysu.usercenter.common.BaseResponse;
import edu.ysu.usercenter.common.ErrorCode;
import edu.ysu.usercenter.exception.BusinessException;
import edu.ysu.usercenter.model.domain.User;
import edu.ysu.usercenter.model.domain.request.UserLoginRequest;
import edu.ysu.usercenter.model.domain.request.UserRegiesterRequest;
import edu.ysu.usercenter.service.UserService;
import edu.ysu.usercenter.utils.ResultUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static edu.ysu.usercenter.constant.UserConstant.ADMIN_ROLE;
import static edu.ysu.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @auther xiaochen
 * @create 2022-06-02 16:18
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;


    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        //获取用户登录态
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;

        //如果当前用户为空，直接返回null
        if (currentUser == null) {
//            return null;
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        //获取当前用户ID
        Long currentUserId = currentUser.getId();
        //TODO 校验用户是否合法

        User user = userService.getById(currentUserId);
        //脱敏
        User result = userService.getSafetyUser(user);
        return ResultUtils.success(result);


    }


    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegiesterRequest userRegiesterRequest) {
        if (userRegiesterRequest == null) {
//            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String userAccount = userRegiesterRequest.getUserAccount();
        String userPassword = userRegiesterRequest.getUserPassword();
        String checkPassword = userRegiesterRequest.getCheckPassword();
        String planetCode = userRegiesterRequest.getPlanetCode();


        /*
         * controller层倾向于对请求参数本身的校验，不涉及业务逻辑本身
         * service层主要是对业务逻辑的校验
         * */
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
//            return null;
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数不能为空！");
        }
        long result = userService.userResgister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(result);

    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
//            return null;
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        }

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        //校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
//            return null;
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数不能为空！");
        }

        User result = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(result);


    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
//            return null;
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }


    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        //鉴权，仅管理员可查询
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH,"您没有权限查询此信息！");
        }

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            userQueryWrapper.like("userName", username);
        }
        List<User> userList = userService.list(userQueryWrapper);

        List<User> result = userList.stream().map(user -> {
            return userService.getSafetyUser(user);
        }).collect(Collectors.toList());

        return ResultUtils.success(result);
    }


    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {

        if (!isAdmin(request)) {
//            return null;
            throw new BusinessException(ErrorCode.NO_AUTH,"您没有权限删除此信息！");
        }
        if (id <= 0) {
//            return null;
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        //仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        if (user == null || user.getUserRole() != ADMIN_ROLE) {
            return false;
        }
        return true;

    }


}
