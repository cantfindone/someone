package com.superjoy.someone.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author Ping
 * @create 2020/11/12 11:27
 */
@Data
@ApiModel("个人动态信息")
public class DatingPostReq {
    @Id
    private String id;

    private String userId;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("文字")
    private String text;
    @ApiModelProperty("图片地址列表")
    private List<String> photos;
    @ApiModelProperty("城市")
    private String city;
    @ApiModelProperty("地标")
    private String location;
    @ApiModelProperty("经纬度")
    private String coordinate;
    @ApiModelProperty("匿名")
    private Boolean anonymous;
    @ApiModelProperty("浏览次数")
    private Long viewers;
    @ApiModelProperty("点赞")
    private Set<String> likes;
    @ApiModelProperty("评论")
    private List<Comment> comments;
    @ApiModelProperty("性别选择")
    private Orientation orientation;

    @Data
    public static class Comment {
        String userId;
        String at;
        String text;
        Date time = new Date();

    }

    public enum Orientation{
        fm,mf,mm,ff
    }
}
