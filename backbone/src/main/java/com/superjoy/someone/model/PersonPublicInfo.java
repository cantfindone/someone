package com.superjoy.someone.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @Author Ping
 * @create 2021/3/31 9:24
 * 字段完全从com.superjoy.someone.model.PersonInfoReq中拷贝
 */
@Data
public class PersonPublicInfo {
    @Id
    private String id;
    @ApiModelProperty("图标")
    private String avatar;
    @ApiModelProperty("昵称")
    private String nick;
    @ApiModelProperty("简介")
    private String info;
    @ApiModelProperty("性别")
    private String gender;
    private Date birth;
    private String city;
    @ApiModelProperty("身高")
    private Float height;
    @ApiModelProperty("体重")
    private Float weight;

    private Boolean authenticated;
}
