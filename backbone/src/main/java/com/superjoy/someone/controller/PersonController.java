package com.superjoy.someone.controller;

import cn.hutool.core.bean.BeanUtil;
import com.superjoy.someone.model.*;
import com.superjoy.someone.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Ping
 * @create 2020/6/10 19:54
 */
@Api(value = "个人信息")
@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/person")
public class PersonController {
    @Autowired
    MongoTemplate db;

    @ApiOperation("保存用户信息")
    @PostMapping("/")
    @ResponseBody
    public PersonInfoReq save(@Valid @RequestBody PersonInfoReq req) {
        PersonInfo p = BeanUtil.toBean(req, PersonInfo.class);
        if (StringUtils.isEmpty(p.getId())) {
            p.setRegTime(new Date());
        }
        p.setRegTime(new Date());
        return db.save(p);
    }

    @ApiOperation("获取用户基本信息")
    @GetMapping("/{id}")
    @ResponseBody
    public PersonPublicInfo get(@PathVariable String id) {
        if(!id.equals(UserContext.currentUserId())) {
            ProfileViews profileViews = db.findById(id, ProfileViews.class);
            if (profileViews == null) {
                profileViews = new ProfileViews(id, 0, new LinkedList<>());
            }
            profileViews.add(new Viewer(UserContext.currentUserId(), new Date()));
            db.save(profileViews);
        }
        PersonInfo personInfo = db.findById(new ObjectId(id), PersonInfo.class);
        log.debug("get PersonInfo by id:{}, res:{}", id, personInfo);
        return BeanUtil.toBean(personInfo, PersonPublicInfo.class);
    }

    @ApiOperation("获取用户基本信息访问记录")
    @GetMapping("/{id}/viewers")
    @ResponseBody
    public ProfileViews viewers(@PathVariable String id) {
        ProfileViews profileViews = db.findById(id, ProfileViews.class);
        if (profileViews == null) {
            profileViews = new ProfileViews(id, 0, new LinkedList<>());
            db.save(profileViews);
        }
        return profileViews;
    }


    @ApiOperation("用户列表")
    @GetMapping(value = "/list")
    @ResponseBody
    public Page<PersonPublicInfo> list(@RequestParam(required = false) Date from,
                                       @RequestParam(required = false) Date to,
                                       @RequestParam(required = false, defaultValue = "0") Integer offset,
                                       @RequestParam(required = false, defaultValue = "10") Integer size) {
        Criteria cri = new Criteria();
        List<Criteria> cs = new ArrayList<>();
        if (from != null) {
            cs.add(Criteria.where("regTime").gte(from));
        }
        if (to != null) {
            cs.add(Criteria.where("regTime").lt(to));
        }
        if (cs.size() != 0) {
            cri.andOperator(cs.toArray(new Criteria[0]));
        }
        Query query = Query.query(cri).with(Sort.by(Sort.Direction.DESC, "regTime"));
        List<PersonPublicInfo> people = db.find(query.limit(size).skip(offset), PersonInfo.class).stream().map(x -> {
            PersonPublicInfo personPublicInfo = BeanUtil.toBean(x, PersonPublicInfo.class);
            personPublicInfo.setAuthenticated(x.isAuthenticated());
            return personPublicInfo;
        }).collect(Collectors.toList());
        return new Page<PersonPublicInfo>(size, offset, db.count(Query.query(cri), PersonInfo.class), people);
    }
}
