package com.forq.demo.controller;

import com.forq.demo.pojo.Authority;
import com.forq.demo.pojo.User;
import com.forq.demo.service.AuthorityService;
import com.forq.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * author long
 *
 * @date 2022/3/4
 * @apiNote
 */
/*8
修改部分：主要是对用户权限 的增加
 对注册用户的接口进行调整 增加用户的权限
 */
@RestController
public class MainController {
    @Autowired
    private UserService userService;
    @Autowired
    private  AuthorityService authorityService;

    private static  final  Long ROLE_USER_AUTHORITY_ID=2L;//用户权限 (也就是博主)

    @GetMapping("/")
    public String root(){
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String login(){
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model){

        model.addAttribute("loginError", true);
        model.addAttribute("errorMsg","登录失败，用户名或者密码错误");
        return "login";
    }
    /**
     * 添加注册用户的方法   registerUser方法
     * @param  user
     * @return
     */
    @PostMapping("/register")
    public String registerUser (User user){

        List<Authority>  authorities=new ArrayList<>();
        authorities.add(authorityService.getAuthorityById(ROLE_USER_AUTHORITY_ID).get());
        user.setAuthorities(authorities);
        /**
         * 针对用户的注册行为 增加权限
         *
         */
        userService.registerUser(user);

        return "redirect:/login";
    }
    /**
     * 当注册成功以后就会
     * 跳转到登录页面  也就是重定向。
     *
     */
    /**
     * 加入权限还不够 还要增加相应的安全配置类才行
     * 搞定配置问题
     */
}

/**
 * 定义主控制器来控制对于页面的访问
 * 实际上就是对于
 */
/**
 * 针对控制器行为的描述
 *
 * 当访问根路径 或index路径时，将会跳转到index.html页面
 *
 * 访问  /login路径时 将会跳转至 login.html页面
 *
 * 登录失败时，将会重定向到 /login-error 路径, 最终会跳转到 login.html页面.
 *
 * 其中在页面中绑定的错误提示信息。
 */

