package com.superjoy.someone.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

/**
 * @Author Ping
 * @create 2020/11/12 11:27
 */
@Data
@ApiModel("个人基本信息")
public class PersonBasicInfoReq {
   @Id
   private String id;
   @ApiModelProperty("图标")
   private String avatar;
   private String name;
   @ApiModelProperty("简介")
   private String info;
   @ApiModelProperty("性别")
   private String gender;
   private Date birth;
   private String phone;
   private String city;
   @ApiModelProperty("身高")
   private Float height;
   @ApiModelProperty("体重")
   private Float weight;
   @ApiModelProperty("婚姻状态")
   private Marriage marriage;
   @ApiModelProperty("性取向")
   private List<Flavour> flavours;
}
