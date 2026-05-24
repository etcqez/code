package com.etcqez.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etcqez.seckill.exception.GlobalException;
import com.etcqez.seckill.mapper.UserMapper;
import com.etcqez.seckill.pojo.User;
import com.etcqez.seckill.service.UserService;
import com.etcqez.seckill.util.CookieUtil;
import com.etcqez.seckill.util.MD5Util;
import com.etcqez.seckill.util.UUIDUtil;
import com.etcqez.seckill.util.ValidatorUtil;
import com.etcqez.seckill.vo.LoginVo;
import com.etcqez.seckill.vo.RespBean;
import com.etcqez.seckill.vo.RespBeanEnum;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();

        String password = loginVo.getPassword();

//        //判断手机号和密码是否为空
//        if (!StringUtils.hasText(mobile) || !StringUtils.hasText(password)) {
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
//        }
//
//        //检验手机号是否合格
//        if (!ValidatorUtil.isMobile(mobile)) {
//            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
//        }

        //查询DB, 看看用户是否存在
        User user = userMapper.selectById(mobile);
        if (null == user) {
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        //如果用户存在，则比对密码!!
        //注意，我们从loginVo取出的密码是中间密码(即客户端经过一次加密加盐处理的密码)
        if (!MD5Util.midPassToDBPass(password, user.getSalt()).equals(user.getPassword())) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }

        //用户登录成功
        //给每个用户生成ticket-唯一
        String ticket = UUIDUtil.uuid();
        //将登录成功的用户保存到session
        request.getSession().setAttribute(ticket, user);
        //将ticket保存到cookie
        CookieUtil.setCookie(request, response, "userTicket", ticket);

        return RespBean.success();
    }
}
