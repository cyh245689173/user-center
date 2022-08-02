package edu.ysu.usercenter.service;


import edu.ysu.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;



/**
 * 用户服务测试
 *
 * @auther xiaochen
 * @create 2022-05-31 10:11
 */
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void testAddUser() {
        User user = new User();

        user.setUserName("laka");
        user.setUserAccount("245689173");
        user.setAvatarUrl("https://tse1-mm.cn.bing.net/th/id/R-C.d1c70e819588207bed6fba871c2d47dd?rik=6P9u%2b7JBZVikQQ&riu=http%3a%2f%2ffile.qqtouxiang.com%2fnansheng%2f2019-12-24%2f156cd442c68a0e20030c0fe72efe7dc9.jpeg&ehk=75MR%2fJ%2bxSvpra0%2btzVMowB%2b4h5cXfcv5U0K2e3xFYfY%3d&risl=&pid=ImgRaw&r=0&sres=1&sresct=1");
        user.setGender((byte) 0);
        user.setUserPassword("123456789");
        user.setPhone("12345678");
        user.setEmail("456");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);


    }

    @Test
    void userResgister() {

        //===========================================================
        //密码为空
        String userAccount = "bubble";
        String userPassword = "";
        String checkPassword = "123456";
        String planetCode = "1";
        long result = userService.userResgister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1, result);


        //===========================================================
        //用户名过短
        userAccount = "bu";
        result = userService.userResgister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1, result);


        //===========================================================
        //密码过短

        userAccount = "bubble";
        userPassword = "123456";
        result = userService.userResgister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1, result);

        //===========================================================
        //用户名包含特殊字符
        userAccount = "bu bble";
        userPassword = "12345678";
        result = userService.userResgister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1, result);


        //===========================================================
        //密码不一致
        checkPassword = "123456789";
        result = userService.userResgister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1, result);

        //===========================================================
        //用户名重复
        userAccount = "123";
        checkPassword = "12345678";
        result = userService.userResgister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1, result);

        //===========================================================
        //注册
        userAccount = "bubble";
        result = userService.userResgister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertTrue(result > 0);


    }
}