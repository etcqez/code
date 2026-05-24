package com.etcqez.seckill.controller;


import com.etcqez.seckill.service.UserService;
import com.etcqez.seckill.vo.LoginVo;
import com.etcqez.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Resource
    private UserService userService;

    //编写方法, 可以进入到登录页面
    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/doLogin")
    @ResponseBody
    public RespBean doLogin(@Valid LoginVo loginVo,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        //log.info("{}", loginVo);//观察数据
        return userService.doLogin(loginVo, request, response);
    }

}
