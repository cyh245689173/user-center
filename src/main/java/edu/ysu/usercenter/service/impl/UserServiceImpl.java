package edu.ysu.usercenter.service.impl;

import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.ysu.usercenter.common.ErrorCode;
import edu.ysu.usercenter.exception.BusinessException;
import edu.ysu.usercenter.model.domain.User;
import edu.ysu.usercenter.service.UserService;
import edu.ysu.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static edu.ysu.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务实现类
 *
 * @author 陈玉皓
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    //注入mapper
    @Resource
    private UserMapper userMapper;


    /**
     * 加个盐
     * 盐值：混淆密码
     */
    private static final String SALT = "xilanhua";


    @Override
    public long userResgister(String userAccount, String userPassword, String checkPassword,String planetCode) {
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }

        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号不得小于4位！");
        }

        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码不得小于8位！");
        }

        //星球编号不能大于5，希望有朝一日可以改成6，嘎嘎嘎
        if (planetCode.length() >5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球编号过长！");
        }



        //账户不能包含特殊字符
        String vaildPattern = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";

        Matcher matcher = Pattern.compile(vaildPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码中不能包含特殊字符！");
        }


        //密码和校验密码相同

        //注：不能用==判断两个字符串，要用equals
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码输入不一致！");
        }


        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        //如果账号已经存在
        if (count > 0) {
//            return -1;
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号已经存在！");
        }


        //星球编号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", userAccount);
        count = userMapper.selectCount(queryWrapper);
        //如果星球编号已经存在
        if (count > 0) {
//            return -1;
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球编号已经存在！");
        }



        //2.代码加密
        final String SALT = "xilanhua";
        //encrypt：加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());


        //3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean saveResulet = this.save(user);

        //保存失败
        if (!saveResulet) {
//            return -1;
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数错误！");
        }

        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {


        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            // todo 修改为自定义异常
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数不能为空！");

        }

        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号不得小于4位！");
        }

        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码不得小于8位！");
        }

        //账户不能包含特殊字符
        String vaildPattern = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";

        Matcher matcher = Pattern.compile(vaildPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码中不能包含特殊字符！");
        }


        //2.代码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());


        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);

        //用户不存在
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("User login failed,userAccount cannot match userPassword!");
            return null;
        }

        //3.用户脱敏
        User safetyUser = getSafetyUser(user);

        //4.记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);


        return safetyUser;


    }


    /**
     * 用户脱敏
     *
     * @param user
     * @return
     */
    @Override
    public User getSafetyUser(User user) {
        if (user == null) {
            return null;
        }

        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUserName(user.getUserName());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setPlanetCode(user.getPlanetCode());
        return safetyUser;

    }

    @Override
    public int userLogout(HttpServletRequest request) {
        //移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}




