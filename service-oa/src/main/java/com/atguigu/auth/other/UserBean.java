package com.atguigu.auth.other;

import org.springframework.stereotype.Component;

/**
 * @author jellyfish
 * @create 2023 -- 08 -- 04 -- 13:08
 * @描述
 * @email:1137648153@qq.com
 */
@Component
public class UserBean {
    public String getUsername(int id) {
        if(id == 1) {
            return "lilei";
        }
        if(id == 2) {
            return "hghghghg";
        }
        return "admin";
    }
}
