package com.superjoy.someone.controller;

import cn.hutool.core.bean.BeanUtil;
import com.superjoy.someone.model.DatingPost;
import com.superjoy.someone.model.DatingPostReq;
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
@Api(value = "相亲贴")
@RestController
@CrossOrigin
@RequestMapping("/datingPost")
public class DatingPostController {
    @Autowired
    MongoTemplate db;
    @ApiOperation("发布")
    @PostMapping("/")
    public DatingPostReq save(@Valid @RequestBody DatingPostReq req) {
        DatingPost p = BeanUtil.toBean(req, DatingPost.class);
        p.setPostTime(new Date());
        return db.save(req);
    }

    @ApiOperation("浏览")
    @PostMapping("/{id}/view")
    public DatingPostReq view(@PathVariable String id, String userId) {
        DatingPostReq post = db.findById(new ObjectId(id), DatingPostReq.class);
        List<String> viewers = post.getViewers();
        viewers.add(userId);
        db.save(post);
        return post;
    }

    @ApiOperation("点赞")
    @PostMapping("/{id}/like")
    public DatingPostReq like(@PathVariable String id, String userId) {
        DatingPostReq post = db.findById(new ObjectId(id), DatingPostReq.class);
        List<String> likes = post.getLikes();
        likes.add(userId);
        db.save(post);
        return post;
    }

    @ApiOperation("评论")
    @PostMapping("/{id}/comment")
    public DatingPostReq comment(@PathVariable String id, @Valid @RequestBody DatingPostReq.Comment comment) {
        DatingPostReq post = db.findById(new ObjectId(id), DatingPostReq.class);
        List<DatingPostReq.Comment> comments = post.getComments();
        comments.add(comment);
        db.save(post);
        return post;
    }


    @ApiOperation("列表")
    @GetMapping(value = "/list")
    public Page<DatingPostReq> list(@RequestParam(required = false) String userId,
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
        return new Page<DatingPostReq>(size, offset, db.count(Query.query(cri), DatingPostReq.class), db.find(query.limit(size).skip(offset), DatingPostReq.class));
    }
}
