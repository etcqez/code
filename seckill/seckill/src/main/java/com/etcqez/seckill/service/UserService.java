package com.etcqez.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.etcqez.seckill.pojo.User;
import com.etcqez.seckill.vo.LoginVo;
import com.etcqez.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService extends IService<User> {

    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);
}
