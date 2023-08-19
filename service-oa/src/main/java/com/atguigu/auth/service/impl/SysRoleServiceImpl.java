package com.atguigu.auth.service.impl;

/**
 * @author jellyfish
 * @create 2023 -- 07 -- 25 -- 21:19
 * @描述
 * @email:1137648153@qq.com
 */

import com.atguigu.auth.mapper.SysRoleMapper;
import com.atguigu.auth.service.SysRoleService;
import com.atguigu.auth.service.SysUserRoleService;
import com.atguigu.model.system.SysRole;
import com.atguigu.model.system.SysUserRole;
import com.atguigu.vo.system.AssginRoleVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ClassName: SysRoleServiceImpl
 * Package: com.jerry.auth.service.impl
 * Description:
 *
 * @Author jerry_jy
 * @Create 2023-03-01 9:13
 * @Version 1.0
 */

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {


    @Autowired
    private SysUserRoleService sysUserRoleService;
    // 2、为用户分配角色
    @Override
    public void doAssign(AssginRoleVo assginRoleVo) {
        //把用户之前分配的角色数据删除，用户角色关系表里面，根据userid删除
        LambdaQueryWrapper<SysUserRole> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId,assginRoleVo.getUserId());
        sysUserRoleService.remove(wrapper);
        //重新进行分配
        List<Long> roleIdList=assginRoleVo.getRoleIdList();
        for (Long roleId:roleIdList){
            if (StringUtils.isEmpty(roleId)){
                continue;
            }
            SysUserRole sysUserRole=new SysUserRole();
            sysUserRole.setUserId(assginRoleVo.getUserId());
            sysUserRole.setRoleId(roleId);
            sysUserRoleService.save(sysUserRole);
        }

    }

    // 1、查询所有角色 和 当前用户所属角色
    @Override
    public Map<String, Object> findRoleDataByUserId(Long userId) {
        //查询所有角色,返回list集合
        List<SysRole> allRoleList = baseMapper.selectList(null);
        //根据用户id查询用户关系表，查询id对应的所有角色id
        LambdaQueryWrapper<SysUserRole> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId,userId);
        List<SysUserRole> exitUserRoleList = sysUserRoleService.list(wrapper);
        //根据查询所有角色id，找到对应角色信息
//        List<Long> list = new ArrayList<>();
//        for (SysUserRole sysUserRole:exitUserRoleList){
//            Long roleId=sysUserRole.getRoleId();
//            list.add(roleId);
//        }
        List<Long> exitRoleList = exitUserRoleList.stream().map(c -> c.getRoleId()).collect(Collectors.toList());

        //根据角色id倒所有的角色的list集合进行比较
        List<SysRole> assignRoleList=new ArrayList<>();
        for (SysRole sysRole:allRoleList){
            if (exitRoleList.contains(sysRole.getId())){
                assignRoleList.add(sysRole);
            }
        }
        //4 把得到两部分数据封装map集合，返回
        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assginRoleList", assignRoleList);
        roleMap.put("allRolesList", allRoleList);
        return roleMap;

    }
}

