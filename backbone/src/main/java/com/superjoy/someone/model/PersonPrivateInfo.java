package com.superjoy.someone.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * @Author Ping
 * @create 2021/3/31 9:18
 */
@Data
public class PersonPrivateInfo {
    @ApiModelProperty("身份证号")
    private String idNo;
    @Indexed(unique = true)
    @ApiModelProperty("手机号")
    private String phone;

    public Boolean isAuthenticated() {
        return StringUtils.isNotEmpty(idNo);
    }
}
