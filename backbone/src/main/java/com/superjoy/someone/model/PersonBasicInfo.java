package com.superjoy.someone.model;

import lombok.Data;

import java.util.Date;

/**
 * @Author Ping
 * @create 2020/11/16 10:06
 */
@Data
public class PersonBasicInfo extends PersonBasicInfoReq{
    private Date updateTime;
    private Date regTime;
}
