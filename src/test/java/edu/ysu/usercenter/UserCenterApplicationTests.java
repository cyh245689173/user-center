package edu.ysu.usercenter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

@SpringBootTest
class UserCenterApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void digest(){
        //2.代码加密
        String newPassword = DigestUtils.md5DigestAsHex("abc".getBytes());
        System.out.println(newPassword);
    }

}
