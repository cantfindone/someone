package com.superjoy.someone.model;

import lombok.Builder;
import lombok.Data;

/**
 * @Author Ping
 * @create 2021/3/30 11:12
 */
@Data
@Builder
public class UserInfo {
    String userId;
    String mobile;
}
