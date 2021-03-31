package com.superjoy.someone.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @Author Ping
 * @create 2021/3/31 15:29
 */
@Data
@AllArgsConstructor
public class Viewer {
    String viewerId;
    Date time;
}
