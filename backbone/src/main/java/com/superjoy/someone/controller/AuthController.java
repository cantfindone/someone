package com.superjoy.someone.controller;

import com.superjoy.someone.model.LoginRes;
import com.superjoy.someone.model.PersonInfo;
import com.superjoy.someone.model.PhoneNoLoginReq;
import com.superjoy.someone.model.UserInfo;
import com.superjoy.someone.utils.JwtUtil;
import com.superjoy.someone.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @ApiOperation("获取手机验证码")
    @GetMapping("/sms/{phone}")
    @ResponseBody
    public String sms(@Valid @PathVariable String phone) {
        return "success";
    }

    @ApiOperation("手机号验证码登录")
    @PostMapping("/phone")
    @ResponseBody
    public LoginRes phone(@Valid @RequestBody PhoneNoLoginReq req) {
        List<PersonInfo> users = db.find(Query.query(Criteria.where("phone").is(req.getMobile())), PersonInfo.class);
        String userId = (users == null || users.size() == 0) ? "unRegistered" : users.get(0).getId();
        String jwt = JwtUtil.createJWT(req.getMobile(), "SuperJoy", userId, ttlMillis);
        return LoginRes.builder().jwt(jwt).mobile(req.getMobile()).userId(userId).expireAt(System.currentTimeMillis() + ttlMillis).build();
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
