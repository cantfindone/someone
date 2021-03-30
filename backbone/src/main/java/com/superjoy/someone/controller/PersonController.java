package com.superjoy.someone.controller;

import cn.hutool.core.bean.BeanUtil;
import com.superjoy.someone.model.Page;
import com.superjoy.someone.model.PersonBasicInfo;
import com.superjoy.someone.model.PersonBasicInfoReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import java.util.List;

/**
 * @Author Ping
 * @create 2020/6/10 19:54
 */
@Api(value = "个人信息")
@RestController
@CrossOrigin
@RequestMapping("/person")
public class PersonController {
    @Autowired
    MongoTemplate db;

    @ApiOperation("保存用户信息")
    @PostMapping("/")
    public PersonBasicInfoReq save(@Valid @RequestBody PersonBasicInfoReq req) {
        PersonBasicInfo p = BeanUtil.toBean(req, PersonBasicInfo.class);
        if (StringUtils.isEmpty(p.getId())) {
            p.setRegTime(new Date());
        }
        p.setRegTime(new Date());
        return db.save(req);
    }

    @ApiOperation("获取用户")
    @GetMapping("/{id}")
    public PersonBasicInfoReq get(@PathVariable String id) {
        return db.findById(new ObjectId(id), PersonBasicInfoReq.class);
    }


    @ApiOperation("用户列表")
    @GetMapping(value = "/list")
    public Page<PersonBasicInfoReq> list(@RequestParam(required = false) Date from,
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
        return new Page<PersonBasicInfoReq>(size, offset, db.count(Query.query(cri), PersonBasicInfoReq.class), db.find(query.limit(size).skip(offset), PersonBasicInfoReq.class));
    }
}
