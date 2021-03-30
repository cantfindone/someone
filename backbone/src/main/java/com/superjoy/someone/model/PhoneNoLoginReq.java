package com.superjoy.someone.model;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

/**
 * @Author Ping
 * @create 2021/3/30 10:14
 */
@ApiModel
@Data
@Builder
public class PhoneNoLoginReq {
    String mobile;
    String smsCode;
}
