package com.example.httpclientstarter.controller;

import com.example.httpclientstarter.pojo.Info;
import com.example.httpclientstarter.pojo.User;
import com.example.httpclientstarter.utils.HttpClientHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/http")
public class TestController {

    @GetMapping("/test")
    public String test() throws IOException {
        User user = new User();
        Info info = new Info();
        info.setImage("2.jpj");
        info.setNumber("111");

        user.setName("zhangsan");
        user.setAge("12");
        user.setInfo(info);

        HttpClientHelper instance = HttpClientHelper.getInstance();
        String result = instance.postJson("http://localhost:8080/httpTest/postTest", user);

        return result;

    }

}
