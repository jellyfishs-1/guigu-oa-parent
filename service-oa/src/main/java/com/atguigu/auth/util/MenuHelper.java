package com.atguigu.auth.util;

import com.atguigu.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jellyfish
 * @create 2023 -- 07 -- 29 -- 11:44
 * @描述
 * @email:1137648153@qq.com
 */

public class MenuHelper {

    public static List<SysMenu> buildTree(List<SysMenu> sysMenuList) {
        // 存放最终数据
        List<SysMenu> trees = new ArrayList<>();

        // 把所有的菜单数据进行遍历
        for (SysMenu sysMenu : sysMenuList) {
            // 递归入口 parentId = 0
            if (sysMenu.getParentId().longValue()==0){
                trees.add(getChildren(sysMenu,sysMenuList));
            }

        }
        return trees;

    }

    /**
     * 递归查找子节点
     * @param sysMenu
     * @param sysMenuList
     * @return
     */
    public static SysMenu getChildren(SysMenu sysMenu,List<SysMenu> sysMenuList){
        sysMenu.setChildren(new ArrayList<SysMenu>());

        // 遍历所有的菜单数据，判断id和parent_id的对应关系
        for (SysMenu menu : sysMenuList) {
            if (sysMenu.getId().longValue() == menu.getParentId().longValue()){
                if (sysMenu.getChildren() == null) {
                    sysMenu.setChildren(new ArrayList<>());
                }
                sysMenu.getChildren().add(getChildren(menu,sysMenuList));
            }
        }
        return sysMenu;
    }

}
