package com.superjoy.someone.controller;

import cn.hutool.core.bean.BeanUtil;
import com.superjoy.someone.model.InstantPost;
import com.superjoy.someone.model.InstantPostReq;
import com.superjoy.someone.model.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Ping
 * @create 2021/3/29 14:16
 */
@Api(value = "个人动态")
@RestController
@CrossOrigin
@RequestMapping("/instantPost")
public class InstantPostController {
    @Autowired
    MongoTemplate db;
    @ApiOperation("发布动态")
    @PostMapping("/")
    public InstantPostReq save(@Valid @RequestBody InstantPostReq req) {
        InstantPost p = BeanUtil.toBean(req, InstantPost.class);
        p.setPostTime(new Date());
        return db.save(req);
    }

    @ApiOperation("浏览")
    @PostMapping("/{id}/view")
    public InstantPostReq view(@PathVariable String id, String userId) {
        InstantPostReq post = db.findById(new ObjectId(id), InstantPostReq.class);
        List<String> viewers = post.getViewers();
        viewers.add(userId);
        db.save(post);
        return post;
    }

    @ApiOperation("点赞")
    @PostMapping("/{id}/like")
    public InstantPostReq like(@PathVariable String id, String userId) {
        InstantPostReq post = db.findById(new ObjectId(id), InstantPostReq.class);
        List<String> likes = post.getLikes();
        likes.add(userId);
        db.save(post);
        return post;
    }

    @ApiOperation("评论")
    @PostMapping("/{id}/comment")
    public InstantPostReq comment(@PathVariable String id, @Valid @RequestBody InstantPostReq.Comment comment) {
        InstantPostReq post = db.findById(new ObjectId(id), InstantPostReq.class);
        List<InstantPostReq.Comment> comments = post.getComments();
        comments.add(comment);
        db.save(post);
        return post;
    }


    @ApiOperation("动态列表")
    @GetMapping(value = "/list")
    public Page<InstantPostReq> list(@RequestParam(required = false) String userId,
                                         @RequestParam(required = false) String  city,
                                         @RequestParam(required = false, defaultValue = "0") Integer offset,
                                         @RequestParam(required = false, defaultValue = "10") Integer size) {
        Criteria cri = new Criteria();
        List<Criteria> cs = new ArrayList<>();
        if (userId != null) {
            cs.add(Criteria.where("userId").is(userId));
        }
        if (city != null) {
            cs.add(Criteria.where("city").is(city));
        }
        if (cs.size() != 0) {
            cri.andOperator(cs.toArray(new Criteria[0]));
        }
        Query query = Query.query(cri).with(Sort.by(Sort.Direction.DESC, "postTime"));
        return new Page<InstantPostReq>(size, offset, db.count(Query.query(cri), InstantPostReq.class), db.find(query.limit(size).skip(offset), InstantPostReq.class));
    }
}
