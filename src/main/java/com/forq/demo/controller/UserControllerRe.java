package com.forq.demo.controller;

/**
 * author long
 *
 * @date 2022/3/5
 * @apiNote
 */

import com.forq.demo.pojo.Authority;
import com.forq.demo.pojo.User;
import com.forq.demo.service.AuthorityService;
import com.forq.demo.service.UserService;
import com.forq.demo.util.ConstraintViolationExceptionHandler;
import com.forq.demo.vo.Response;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 修改好UserController
 * 处理对API的不同请求
 * 一共有多少的API
 *
 */

@RestController
@RequestMapping("/users")
/**
 * 针对 users接口下的各种操作
 */

public class UserControllerRe {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;
    /**
     * 注入业务是必须的
     * 并且测试也可以先从控制层先测
     */
    /**
     * 查询所有用户
     *
     * @param async
     * @param pageindex
     * @param pagesize
     * @param name
     * @param model
     *
     * @return
     *
     */
    /**
     *针对 requestparam  required 属性 要求 false 则表示如果不传参数也就是意味着参数为Null,
     * 如果值是true 则表示参数一定要传入
     */
    @GetMapping
    public ModelAndView list(
            @RequestParam(value="async" ,required = false) boolean async,
            @RequestParam(value="pageindex",required = false,defaultValue = "0") int pageindex,
            @RequestParam(value="pagesize",required= false ,defaultValue = "10") int pagesize,
            @RequestParam(value="name",required = false,defaultValue = "")String name,
            Model model){
        /**
         * 最后一个参数反而不用带注解
         *
         */
        Pageable pageable= PageRequest.of(pageindex,pagesize);
        //这句话的意思是 加入 分页索引值 和分页尺寸 构成分页查询条件
        Page<User> page=userService.listUsersByNameLike(name,pageable);
        //这句话的意思是 利用依赖注入的业务层对象来完成 模糊查询并且 变成分页
        List<User> list =page.getContent();
        // 保证当前页的列表
        model.addAttribute("page",page);
        model.addAttribute("userlist",list);
        /**
         *  先把最后一行代码注释一下
         *
         */
        return new ModelAndView(async ==true? "users/list :: #mainContainerRepleace":"users/list","userModle",model);
        /**
         * 返回了模型视图
         * 这里也就是说  如果异步为真 则返回到
         *  如果异步为加 则 返回到
         *
         *
         */


    }
    /**
     * 获取创建表单页面
     * @param model
     * @return
     */
    @GetMapping("/add")
    public ModelAndView createForm (Model model){
        /**
         * 所谓的模型 就是属性名加属性值
         * 而模型视图 则是表示对应模型会配对哪一个视图层
         * 将模型and视图返回 并且交给前端控制器去解析 然后返回响应 最终进行页面的渲染
         *
         */
        model.addAttribute("user", new User());
        /**
         * 构建的一个 属性名为user 属性值为空user对象的模型
         */
        return  new ModelAndView("users/add","userModel",model);
        /**
         * 让他与 user/add 新增的视图进行配对
         */
        /**
         * 这样的话就可以针对对应的视图做出 相匹配的数据更新
         */
    }
    /**
     * 保存或修改用户
     * @param user
     * @return

     */

    @PostMapping
    public ResponseEntity<Response> saveOrUpdateUser(User user
            ,Long authorityId){

        /**
         * 修改代码 主要是加入了权限认证这块
         * 让不同的角色有不同的权限
         *
         */
        List<Authority> authorities=new ArrayList<>();
        authorities.add(authorityService.getAuthorityById(authorityId).get());
        user.setAuthorities(authorities);//对用户对象添加权限

        /**
         * 没想到我搞错了 主要是 用户的实体类 没有加 lombock 下次要注意改正
         */
        try{

            userService.svaeOrUpdateUser(user);
        }catch (ConstraintViolationException e){

            return ResponseEntity.ok().body(new Response(false,

                    ConstraintViolationExceptionHandler.getMessage(e)));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功",user));
    }
    /**
     * 对 saveOrUpdateUser方法进行二次修改
     * 主要是因为 要加入权限控制
     */
    /**
     * 参数时 user 也就是说表单提交数据以后
     * 就保存或者更新的用户
     * 并且返回 response 的响应体
     * 但是成不成功还需要 经过检验
     * 所以采用try catch 结构进行处理
     * 如果没有发生异常
     * 就直接返回响应实体
     * 如果发生了异常
     * 则返回带有异常信息的响应实体

     */


    /**
     *
     * 删除用户
     * @param  id
     * @param  model
     * @return
     *
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response> delete (@PathVariable("id") Long id, Model model){

        /**
         * 首先要声明的是 我认为每一个待返回的结果都应该是 已经封装好的结果response
         * 不管会不会返回值
         *
         */
        try{
            userService.removeUser(id);
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok()
                .body(new Response(true,"处理成功"));
        /**
         * 响应实体调用 ok 方法的意思就在于返回ok状态的同时也返回响应实体
         *
         */
    }
    /**
     * 获取 修改用户的界面
     *
     * @param  id
     * @param  model
     * @return
     */
    @GetMapping(value = "edit/{id}")
    public ModelAndView modifyForm(@PathVariable("id") Long id, Model model){
        Optional<User> user =userService.getUserById(id);
        /**
         * 业务层的代码注入并调用根据id 来查找 对象 并把返回的对象 用optional容器装起来
         */
        /**
         * 这个Optional有什么作用？
         * Optional 来构建 一个对象容器 并且提醒 用户要注意
         * 该对象可能为null
         * Optional.of(T t)
         * 创建一个对象容器并加入对象 如果对象为空 就会抛出异常
         * Optional.ofNullable(T t)
         * 如果对象为空 不会抛出异常 但是会返回一个空的实例
         */
        model.addAttribute("user",user.get());
        return new ModelAndView("users/edit","userModel",model);
        /**
         * 最终容器的get 表示获取其内在对象实例
         * 并且构成模型
         * 最终返回 视图模型
         */


    }
    /**
     * 编写menu类的关键
     * 放置用户管理的菜单项
     */






}

