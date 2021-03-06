package com.superjoy.someone.model;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Ping
 * @create 2021/3/30 10:24
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
public class LoginRes {
    String userId;
    String mobile;
    String jwt;
    Long expireAt;

}
