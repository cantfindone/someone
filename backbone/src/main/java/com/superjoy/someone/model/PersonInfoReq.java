package com.superjoy.someone.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @Author Ping
 * @create 2020/11/12 11:27
 */
@Data
@ApiModel("个人信息")
public class PersonInfoReq extends PersonPrivateInfo {
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
}
