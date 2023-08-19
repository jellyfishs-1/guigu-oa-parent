package com.atguigu.common.config.exception;

import com.atguigu.common.result.ResultCodeEnum;
import io.swagger.models.auth.In;
import lombok.Data;

/**
 * @author jellyfish
 * @create 2023 -- 07 -- 26 -- 14:59
 * @描述
 * @email:1137648153@qq.com
 */
@Data
public class GuiguException extends RuntimeException{

    private Integer code;
    private String msg;

    public GuiguException(Integer code,String msg){
        super(msg);
        this.code=code;
        this.msg=msg;

    }

    /**
     * 接收枚举类型对象
     * @param resultCodeEnum
     */
    public GuiguException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getMessage();
    }

    @Override
    public String toString() {
        return "GuiguException{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

}
