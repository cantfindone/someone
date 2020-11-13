package com.superjoy.someone.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author Ping
 * @create 2020/11/12 11:27
 */
@Data
public class PersonBasicInfo {
   private String avatar;
   private String name;
   private String info;
   private String gender;
   private Date birth;
   private Date regTime;
   private Date updateTime;
   private String city;
   private Float height;
   private Float weight;
   private Marriage marriage;
   private List<Flavour> flavours;
}
