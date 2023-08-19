package com.atguigu.auth.controller;

import com.atguigu.auth.service.SysMenuService;
import com.atguigu.auth.service.SysRoleService;
import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysRole;
import com.atguigu.vo.system.AssginRoleVo;
import com.atguigu.vo.system.SysRoleQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author jellyfish
 * @create 2023 -- 07 -- 25 -- 21:44
 * @描述
 * @email:1137648153@qq.com
 */
@Api(tags = "角色管理接口")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysMenuService sysMenuService;



//    @GetMapping("/findAll")
//    public List<SysRole> findAll(){
//
//        List<SysRole> list = sysRoleService.list();
//
//        return list;
//    }

    @ApiOperation("查询所有角色")
    @GetMapping("/findAll")
    public Result findAll(){

        List<SysRole> list = sysRoleService.list();

//        try {
//            int i=10/0;
//        } catch (Exception e) {
//            throw new GuiguException(20001,"执行了自定义异常处理。。");
//        }
        return Result.ok(list);
    }


    /**
     *
     * @param page 当前页
     * @param limit 每页记录数
     * @param sysRoleQueryVo 条件对象
     * @return
     */
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result pageQueryRole(@PathVariable Long page, @PathVariable Long limit,
                                SysRoleQueryVo sysRoleQueryVo){

        //创建page对象，传递分页相关参数
        Page<SysRole> pageParam = new Page<>(page, limit);

        //封装条件，判断条件是否为空，不为空进行封装
        LambdaQueryWrapper<SysRole> wrapper=new LambdaQueryWrapper<>();

        String roleName = sysRoleQueryVo.getRoleName();
        if (!StringUtils.isEmpty(roleName)){
            wrapper.like(SysRole::getRoleName,roleName);
        }

        Page<SysRole> pageModel = sysRoleService.page(pageParam, wrapper);

        return Result.ok(pageModel);
    }


    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @ApiOperation("添加角色")
    @PostMapping("save")
    public Result save(@RequestBody SysRole role){
        //调用service的方法
        boolean is_success = sysRoleService.save(role);
        if (is_success){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    //修改角色
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("根据ig查询角色")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id){
        SysRole sysRole = sysRoleService.getById(id);
        return Result.ok(sysRole);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @ApiOperation("修改角色")
    @PutMapping("update")
    public Result update(@RequestBody SysRole role){
        //调用service的方法
        boolean is_success = sysRoleService.updateById(role);
        if (is_success){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("根据id删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        boolean is_success = sysRoleService.removeById(id);
        if (is_success){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("批量删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList){
        boolean is_success = sysRoleService.removeByIds(idList);
        if (is_success){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    // 1、查询所有角色 和 当前用户所属角色
    @ApiOperation("根据用户获取角色数据")
    @GetMapping("/toAssign/{userId}")
    public Result toAssign(@PathVariable Long userId) {
        Map<String, Object> map = sysRoleService.findRoleDataByUserId(userId);
        return Result.ok(map);
    }

    // 2、为用户分配角色
    @ApiOperation("为用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginRoleVo assginRoleVo) {
        sysRoleService.doAssign(assginRoleVo);
        return Result.ok();
    }



}
