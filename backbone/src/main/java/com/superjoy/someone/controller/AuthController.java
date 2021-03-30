package com.superjoy.someone.controller;

import com.superjoy.someone.model.LoginRes;
import com.superjoy.someone.model.PhoneNoLoginReq;
import com.superjoy.someone.model.UserInfo;
import com.superjoy.someone.utils.JwtUtil;
import com.superjoy.someone.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author Ping
 * @create 2021/3/30 10:11
 */

@Api(value = "用户登录")
@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    MongoTemplate db;
    int ttlMillis = 24 * 60 * 60 * 1000;

    @ApiOperation("手机号登录")
    @PostMapping("/phone")
    @ResponseBody
    public LoginRes phone(@Valid @RequestBody PhoneNoLoginReq req) {
        String jwt = JwtUtil.createJWT(req.getMobile(), "SuperJoy", "unRegistered", ttlMillis);
        return LoginRes.builder().jwt(jwt).mobile(req.getMobile()).userId("unRegistered").expireAt(System.currentTimeMillis()+ttlMillis).build();
    }

    @ApiOperation("刷新token")
    @PostMapping("/refresh")
    @ResponseBody
    public LoginRes refresh() {
        UserInfo current = UserContext.current();
        String newJwt = JwtUtil.createJWT(current.getMobile(), "SuperJoy", current.getUserId(), ttlMillis);
        return LoginRes.builder().jwt(newJwt).mobile(current.getMobile()).userId("unRegistered").expireAt(System.currentTimeMillis() + ttlMillis).build();
    }

}
