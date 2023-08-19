package com.atguigu.auth.service;

/**
 * @author jellyfish
 * @create 2023 -- 07 -- 25 -- 21:18
 * @描述
 * @email:1137648153@qq.com
 */


import com.atguigu.model.system.SysRole;
import com.atguigu.vo.system.AssginRoleVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * ClassName: SysRoleService
 * Package: com.jerry.auth.service
 * Description:
 *
 * @Author jerry_jy
 * @Create 2023-03-01 9:12
 * @Version 1.0
 */


public interface SysRoleService extends IService<SysRole> {

    // 2、为用户分配角色
    void doAssign(AssginRoleVo assginRoleVo);

    // 1、查询所有角色 和 当前用户所属角色
    Map<String, Object> findRoleDataByUserId(Long userId);
}
