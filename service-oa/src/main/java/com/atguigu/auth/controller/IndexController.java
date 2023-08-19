package com.atguigu.auth.controller;

import com.atguigu.auth.service.SysMenuService;
import com.atguigu.auth.service.SysUserService;
import com.atguigu.common.config.exception.GuiguException;
import com.atguigu.common.jwt.JwtHelper;
import com.atguigu.common.jwt.MD5;
import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysUser;
import com.atguigu.vo.system.LoginVo;
import com.atguigu.vo.system.RouterVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jellyfish
 * @create 2023 -- 07 -- 27 -- 13:16
 * @描述
 * @email:1137648153@qq.com
 */
@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    //login
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo){
//        Map<String,Object> map=new HashMap<>();
//        map.put("token","admin-token");
//        return Result.ok(map);

        //获取用户名和密码

        //根据用户名查询数据库
        String username = loginVo.getUsername();
        LambdaQueryWrapper<SysUser> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername,username);
        SysUser sysUser = sysUserService.getOne(wrapper);


        //用户信息是否存在
        if (sysUser==null){
            throw new GuiguException(201,"用户不存在");
        }

        //判断密码
        String password_db = sysUser.getPassword();
        //获取输入的密码
        String password = loginVo.getPassword();
        String password_input = MD5.encrypt(password);

        if (!password_db.equals(password_input)){
            throw new GuiguException(201,"密码错误");
        }

        //判断用户是否被禁用 1可用 0 禁用
        if (sysUser.getStatus().intValue()==0){
            throw new GuiguException(201,"用户已经被禁用");
        }

        //使用jwt根据用户id和用户名生成token字符串
        String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());

        Map<String,Object> map=new HashMap<>();
        map.put("token",token);
        return Result.ok(map);
    }

    //info
    @GetMapping("info")
    public Result info(HttpServletRequest request){
        //1.从请求头获取用户信息
        String token = request.getHeader("token");

        //从token字符串串获取用户id或者用户名称
        Long userId = JwtHelper.getUserId(token);

        //根据用户id查询数据库，把用户信息获取出来
        SysUser sysUser = sysUserService.getById(userId);
        //根据用户id获取用户可用操作的菜单列表
        List<RouterVo> routerVoList=sysMenuService.findUserMenuListByUserId(userId);

        //根据用户id获取用户可用操作按钮列表
        List<String> permsList=sysMenuService.findUserPermsByUserId(userId);


        //返回相应的数据

        Map<String,Object> map=new HashMap<>();
        map.put("roles","[admin]");
        map.put("name",sysUser.getName());
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        //返回用户可用操作菜单
        map.put("routers",routerVoList);

        //返回用户可操作按钮
        map.put("buttons",permsList);
        return Result.ok(map);
    }

    @PostMapping("logout")
    public Result logout(){
        return Result.ok();
    }
}
