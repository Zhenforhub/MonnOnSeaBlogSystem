package com.forq.demo.controller;

import com.forq.demo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * author long
 *
 * @date 2022/3/4
 * @apiNote
 */
@RestController
@RequestMapping("/users")
public class UserController {
/*    @Autowired
    private UserRespository userRespository;
    *//**
     * 查询所有用户
     * 这是我们的模拟的业务需求
     * @param model
     * @return
     * 这里涉及一个重要的类那就是Modelandview
     *//*
    @GetMapping
    public ModelAndView list(Model model){
        model.addAttribute("userlist",userRespository.listUser());
        model.addAttribute("title", "用户管理");
        return new ModelAndView("users/list","userModel",model);
        *//**
         * 实际上这一段指的是模型与视图进行绑定
         * 视图名已经确定  users/list
         * 然后模型名也确定 usermodel
         *//*
        *//**
         * 并且映射器采用了默认的配置
         *//*

    }
    *//**
     * 根据id 查询用户
     * @param  id
     * @param model
     * @return
     *//*
    @GetMapping("{id}")
    public ModelAndView view(@PathVariable("id") Long id,Model model){
        User user =userRespository.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("title", "查看用户");
        return new ModelAndView("users/view","usermodel",model);
        *//**
         * 强调了模型与视图之间的关系
         * 一个新的modelandview总需要有 视图名 模型名 模型值
         *//*
    }
    *//**
     * 获取创建表单页面
     * @param  model
     * @return
     *//*
    @GetMapping("/form")
    public ModelAndView createForm(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("title", "创建用户");
        return new ModelAndView("users/form","userModel",model);

    }
    *//**
     * 保存用户
     * @param user
     * @return
     *//*
    @PostMapping
    public ModelAndView saveOrUpdateUser(User user){
        user=userRespository.saveOrUpdateUser(user);
        return new ModelAndView("redirect:/users");
    }
    *//**
     * 删除用户
     * @param id
     * @return
     *//*
    @GetMapping(value="delete/{id}")
    public ModelAndView delete(@PathVariable("id") Long id){
        userRespository.deleteUser(id);
        return new ModelAndView("redirect:/users");

    }
    *//**
     * 获取修改用户的界面
     * @param id
     * @param  model
     * @return
     *
     *//*
    @GetMapping(value="modify/{id}")
    public ModelAndView modifyForm(@PathVariable("id") Long id,Model model){

        User user =userRespository.getUserById(id);
        model.addAttribute("user", user);

        model.addAttribute("title", "修改用户");

        return new ModelAndView("users/form","userModel",model);

        }*/

    /**
     * 最后就是API了
     *
     *
     */


}
/**
 * GET/users:返回用于展示用户列表的list.html页面
 * GET/users/{id}:返回用于展现用户的view.html页面
 *
 */
/**
 * 有一点可以肯定 那就是为了模拟前端条件
 * 对前台的页面进行编写也是必要的
 * 并且采用Thymeleaf引擎来开发，页面也比较快捷
 *
   在templete目录下新建uses目录，用来归档用户管理
 的相关功能页面
 list.html 用来展示用户列表
 form.html 用来新增或者修改用户的资料
 view.html 用于查看某个用户的资料

 归档页面共用的模块
 header.html 共用的头部页面
 footer.html 为共用的脚部页面
 */
