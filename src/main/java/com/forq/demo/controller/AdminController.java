package com.forq.demo.controller;

import com.forq.demo.vo.Menu;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * author long
 *
 * @date 2022/3/5
 * @apiNote
 */
@RestController
@RequestMapping("/admin")//加上接口 ，表示这是管理员可以访问的 控制器
public class AdminController {
    /**
     * 究竟有什么作用？
     * 获取后台管理主页面
     * @param model
     * @return
     */
    /**
     * 针对它的功能描述就是说
     * 当请求进入后台管理界面时，返回一个菜单列表这个菜单列表目前只有一个用户管理
     * 但是实际上管理员是会管理所有的后台功能模块
     * 随着功能的丰富，会有越来越多的列表元素。

     */
    @GetMapping
    public ModelAndView listUsers(Model model){
        List<Menu> list=new ArrayList<>();
        /*
          首先定义个Menu 元素列表
         */
        list.add(new Menu("用户管理","/users"));
        //列表 增加了一个 用户管理的菜单，该菜单 具备菜单名和 地址

        model.addAttribute("list",list);
        //对传入的模型 增加其属性 名 list  并传入 list列表
        return  new ModelAndView("/admins/index","model",model);
        //将模型与视图进行绑定 这样就知道了要对哪个视图进行操作了

    }
}
