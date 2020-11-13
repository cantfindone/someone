package com.superjoy.someone.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author Ping
 * @create 2020/4/28 11:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Page <T> {
    int size;
    int offset;
    @Builder.Default
    long total = -1;
    List<T> data;
}
