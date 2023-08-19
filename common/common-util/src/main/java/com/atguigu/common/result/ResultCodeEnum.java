package com.atguigu.common.result;

import lombok.Getter;

/**
 * @author jellyfish
 * @create 2023 -- 07 -- 26 -- 12:36
 * @描述
 * @email:1137648153@qq.com
 */
@Getter
public enum ResultCodeEnum {
    SUCCESS(200, "成功"),
    FAIL(201, "失败"),
    SERVICE_ERROR(2012, "服务异常"),
    DATA_ERROR(204, "数据异常"),
    LOGIN_AUTH(208, "未登陆"),
    PERMISSION(209, "没有权限"),
    LOGIN_MOBLE_ERROR(208, "认证失败");

    private Integer code;
    private  String message;


    ResultCodeEnum(Integer code, String message) {
        this.code=code;
        this.message=message;
    }
}
