package com.superjoy.someone.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.superjoy.someone.model.Page;
import com.superjoy.someone.model.PersonBasicInfo;
import io.swagger.annotations.ApiOperation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicUpdate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Ping
 * @create 2020/6/10 19:54
 */
@RestController
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
@CrossOrigin
@RequestMapping("/person")
public class PersonInfoController
{
    @Autowired
    MongoTemplate db;

    @ApiOperation("保存用户信息")
    @PostMapping("/")
    public PersonBasicInfo save(@Valid @RequestBody PersonBasicInfo req) {
        req.setRegTime(new Date());
        return db.save(req);
    }

    @ApiOperation("获取用户")
    @GetMapping("/{id}")
    public PersonBasicInfo get(@PathVariable String id) {
        return db.findById(new ObjectId(id), PersonBasicInfo.class);
    }


    @ApiOperation("用户列表")
    @GetMapping(value = "/list")
    public Page<PersonBasicInfo> list(@RequestParam(required = false) Date from,
                                      @RequestParam(required = false) Date to,
                                      @RequestParam(required = false, defaultValue = "0") Integer offset,
                                      @RequestParam(required = false, defaultValue = "10") Integer size) {
        Criteria cri = new Criteria();
        List<Criteria> cs=new ArrayList<>();
        if (from != null) {
            cs.add(Criteria.where("submitTime").gte(from));
        }
        if (to != null) {
            cs.add(Criteria.where("submitTime").lt(to));
        }
        if(cs.size()!=0) {
            cri.andOperator(cs.toArray(new Criteria[0]));
        }
        Query query = Query.query(cri).with(Sort.by(Sort.Direction.DESC, "submitTime"));
        return new Page<PersonBasicInfo>(size, offset, db.count(Query.query(cri), PersonBasicInfo.class), db.find(query.limit(size).skip(offset), PersonBasicInfo.class));
    }
}
