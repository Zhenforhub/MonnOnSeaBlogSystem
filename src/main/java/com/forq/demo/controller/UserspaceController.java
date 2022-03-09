package com.forq.demo.controller;

import com.forq.demo.dao.UserRespository;
import com.forq.demo.pojo.Blog;
import com.forq.demo.pojo.User;
import com.forq.demo.pojo.Vote;
import com.forq.demo.service.BlogService;
import com.forq.demo.service.UserService;
import com.forq.demo.util.ConstraintViolationExceptionHandler;
import com.forq.demo.vo.Response;
import javafx.collections.ModifiableObservableListBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sun.plugin.com.PropertyGetDispatcher;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

/**
 * author long
 *
 * @date 2022/3/7
 * @apiNote
 */
@RestController
@RequestMapping("/u/")
public class UserspaceController {

    /**
     * 这就是用户可以进行操作的个人博客空间 对外API暴露处
     *
     */
    @Autowired
    private UserService userService;


    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BlogService blogService;

    @Value("${file.server.url}")
    /**
     * 加上注解以后，获取了文件服务器的url地址，根据此地址来进行文件的上传和更新
     */
    private String fileServerUrl;

    /**
     * 用户的主页
     *
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}")
    public String userSpace(@PathVariable("username") String username,Model model){
        User user= (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        return "redirect:/u/"+username+"/blogs";
        /**
         * 通过这个方式最后
         * 返回用户的界面
         * 因为它由重定向的功能
         */

    }
    /**
     * 获取用户的博客列表
     * @param user
     * @param order
     * @param catalogId
     * @param  keyword
     * @param async
     * @param pageIndex
     * @param pageSize
     * @param  model
     * @return
     */
    @GetMapping("/{username}/blogs")
    public String listBlogsByOrder(@PathVariable("username") String username
            ,@RequestParam(value="order",required = false,defaultValue = "new") String order
              ,@RequestParam(value="catalog",required = false) Long catalogId
              ,@RequestParam(value="keayword",required = false ,defaultValue = "")String keyword
              ,@RequestParam(value="async" ,required = false) boolean async
              ,@RequestParam(value="pageIndex",required = false,defaultValue = "0")int pageIndex
              ,@RequestParam(value="pageSize", required = false,defaultValue = "20") int pageSize
              ,Model model){
        User user=(User)userDetailsService.loadUserByUsername(username);
        Page<Blog> page=null;
        if(catalogId!=null&& catalogId>0){//分类查询
            //TODO
        }else if(order.equals("hot")){//最热查询
            Sort sort=null;
            //  sort=new Sort(Sort.Direction.DESC,"readSize","commentSize","voteSize");//这一行的爆红，但是不知道应该怎么解决先注释掉
            Pageable pageable= PageRequest.of(pageIndex,pageSize,sort);
            page= blogService.listBlogsByTitleVote(user,keyword,pageable);
        }else if(order.equals("new")){//最新查询
            Pageable pageable=PageRequest.of(pageIndex,pageSize);
            page=blogService.listBlogsByTitleVote(user,keyword,pageable);
        }

        List<Blog> list=page.getContent();//当前所在页面的数据列表

        model.addAttribute("user",user);
        model.addAttribute("order",order);
        model.addAttribute("catalogId",catalogId);
        model.addAttribute("keyword",keyword);
        model.addAttribute("page",page);
        model.addAttribute("blogList",list);




        return (async==true?"/userspace/u:#mainContainerRepleace":"/userspace/u");

    }
    /**
     * 获取博客展示界面
     *
     * @param username
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs/{id}")
    public String getBlogById(@PathVariable("username")String username,@PathVariable("id")Long id,Model model){
        User pricipal =null;
        Optional<Blog> blog=blogService.getBlogById(id);

        //每次的读取 ，简单的认为阅读量增加1次
        blogService.readingIncrease(id);

        //判断用户是否为博客的所有者
        boolean isBlogOwner =false;
        if(SecurityContextHolder.getContext().getAuthentication() !=null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
       &&SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser") ){
            pricipal =(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(pricipal!=null&& username.equals(pricipal.getUsername())){
                 isBlogOwner=true;
            }
        }
        model.addAttribute("isBlogOwner",isBlogOwner);
        model.addAttribute("blogModel",blog.get());
        return "/userspace/blog";

    }
    /**
     * 获取新增博客的界面
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs/edit")
    public ModelAndView createBlog(@PathVariable("username") String username,Model model){
        model.addAttribute("blog",new Blog(null,null,null,null,null,null,null,null,null,null));
        model.addAttribute("fileServerUrl",fileServerUrl);

        //文件服务器的地址返回给客户端
        return new ModelAndView("/userspace/blogedit","blogModel",model);
    }
    /**
     * 获取编辑博客的界面
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/{username}/blogs/edit/{id}")
    public ModelAndView editBlog(@PathVariable("username") String username
                                 ,@PathVariable("id") Long id
            ,Model model){
        model.addAttribute("blog",blogService.getBlogById(id).get());
        model.addAttribute("fileServerUrl",fileServerUrl);
        return new  ModelAndView("/userspace/blogedit","blogModel",model);


    }
    /**
     * 保存博客
     * @param username
     * @param blog
     * @return
     */
    @PostMapping("/{username}/blogs/edit")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveBlog(@PathVariable("username") String username,@RequestBody Blog blog){
        try{
            //先判断修改还是新增
            if(blog.getId()!=null){
                Optional<Blog> optionalBlog=blogService.getBlogById(blog.getId());
                if(optionalBlog.isPresent()){
                    Blog orignalBlog =optionalBlog.get();
                    orignalBlog.setTitle(blog.getTitle())
                    .setContent(blog.getContent());
                    orignalBlog.setSummary(blog.getSummary());

                    blogService.saveBlog(orignalBlog);
                }
            }else {
                User user=(User) userDetailsService.loadUserByUsername(username);
                blog.setUser(user);
                blogService.saveBlog(blog);
            }

        }catch (ConstraintViolationException e){
            return ResponseEntity
                    .ok()
                    .body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }catch (Exception e){
            return ResponseEntity
                    .ok()
                    .body(new Response(false,e.getMessage()));
        }
        String redireictUrl ="/u/"+username+"/blogs"+blog.getId();
        return ResponseEntity
                .ok()
                .body(new Response(true,"处理成功",redireictUrl));
    }
    /**
     * 删除博客
     * @param username
     * @param id
     * @return
     */
    @DeleteMapping("/{username}/blogs/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> deleteBlog(@PathVariable("username") String username
            ,@PathVariable("id") Long id){
        try{
            blogService.removeBlog(id);
        }catch (Exception e){
            return ResponseEntity
                    .ok()
                    .body(new Response(false,e.getMessage()));
        }
        String redirectUrl="/u/"+username+"/blogs";
        return ResponseEntity
                .ok()
                .body(new Response(true,"处理成功",redirectUrl));
    }
    /**
     * 其中，获取博客列表的方法
     * 由于获取博客列表的方法 对于分类查询的判断没有具体实现
     * 后面才能
     */






    /**
     * 获取 个人设置页面
     * @param username
     * @param model
     * @return
     */


    @GetMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    /**
     * 提前授权 将授权名必须与username名相等
     */
    public ModelAndView profile(@PathVariable("username") String username, Model model){

        User user= (User)userDetailsService.loadUserByUsername(username);

        /**
         * 虽然说没有被初始化 userDetailsService 并没有给实现类 但是应该是被实现了的工具类
         */
        model.addAttribute("user",user);
        model.addAttribute("fileServerUrl",fileServerUrl);

        /**
         * 文件服务器的地址返回给客户端
         */
        return new ModelAndView("/userspace/profile","usermodel",model);


    }
    /**
     * 保存个人设置
     * @param username
     * @param user
     * @return
     */


    @PostMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    /**
     * PreAuthorize的意思是说只要用户自己才有权限修改自己的个人资料
     * 这里要强调一点
     * 保存用户资料
     * 要先判断用户的密码是否做了修改
     * 如果做了修改，
     * 就要在新密码加密之后保存
     */
    public String savaProfile(@PathVariable("username") String username,User user){

        User oringinalUser =userService.getUserById(user.getId()).get();
        /**
         * 前后user使用同一id id是唯一不变的
         * 从中得到原生的id
         *
         */


        oringinalUser.setEmail(user.getEmail());
        oringinalUser.setName(user.getName());
        /**
         * 用户经过修改  所以要对原生的user 进行填充
         * 主要是填充 修改user用户 的属性
         */

        String rawPassword=oringinalUser.getPassword();
        /**
         * 这里的密码都是经过加密之后的密码
         */
        /**
         * 还要去判断 密码是否经过修改
         */
        PasswordEncoder encoder=new BCryptPasswordEncoder();
        /**
         * 很此案 encoder是一个解码器 是一个抽象类 PassWordEncoder 去接收一个 具体的BCryptPasswordEncoder
         *
         */

        String encodePasswd=encoder.encode(user.getPassword());
        /**
         * 通过解码器的解码方法 user的密码 还原成  字符串原始密码
         */
        /**
         * 加密的方法都是采用PasswordEncoder 方法 并且 利用的是 它的实现类 BCryptPasswordEncoder
         *
         *
         */
        boolean isMatch=encoder.matches(rawPassword,encodePasswd);
        /**
         * 定义一个boolean isMatch 匹配 解码后de 用户原生密码 和 用户目前密码
         */
        if(!isMatch){
            oringinalUser.setEnCodePassWord(user.getPassword());
            /**
             * 密码必须经过加密才能保存到数据库中
             * 保证数据的安全性
             */
        }
        /**
         * 只有当这两个密码不相同的时候，才需要将用户的原生密码 设置成用户目前密码
         */
        userService.svaeOrUpdateUser(oringinalUser);
        /**
         * 最终实现用户的更新 不管是保存还是更新都是使用 同一个方法来做
         */

        /**
         * 当实现用户更新以后，返回重定向到当前页面 并且刷新
         */
        return "redirect:/u/"+username+"/profile";

        /**
         * 保存完设置以后开始跳转
         *
         */
    }
    /**
     * 获取编辑头像的界面
     * @param  username
     * @param  model
     * @return
     */
    @GetMapping("/{username}/avatar")

    @PreAuthorize("authentication.name.equals(#username)")

    public ModelAndView avatar(@PathVariable("username") String username,Model model){

        User user=(User) userDetailsService.loadUserByUsername(username);

        model.addAttribute("user", user);

        return new ModelAndView("/userspace/avatar","usermodel",model);
    }
    /**
     * 获取视图编辑页面 肯定是有相关的页面才能做
     * 但是对于我来说就是 我只需要将相关的页面与模型进行绑定就可以
     */

    @PostMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveAvatar(@PathVariable("username") String username,@RequestBody User user){

        String avatarUrl=user.getAvatar();
        User originalUser=userService.getUserById(user.getId()).get();
        originalUser.setAvatar(avatarUrl);
        return ResponseEntity
                .ok()
                .body(new Response(true,"处理成功",avatarUrl));
        /**
         * 保存了avatar的url地址
         * 但是实际上是有小型文件服务器进行保存
         * 小型文件服务器就不是只保存数据了 而是将数据联通数据流一并保存
         */

    }
    /**
     * 保证只有用户自己才能修改自己的头像
     *



/**
 *
 * 接下来是新增数据
 * 也即是保存头像
 * @param username
 * @param model
 * @return
 *
 */


    /**
     * 修改 UserspaceControler 增加 判断用户的点赞情况
     *
     *
     */
    @GetMapping("/{username}/blogs/{id}")
    public String getBlogById(@PathVariable("username") String username
            , @PathVariable("id") Long id
            ,Model model) {

        Optional<Blog> optionalblog = blogService.getBlogById(id);

        Blog blog = null;
        Vote currentVote = null;

        if (optionalblog.isPresent()) {
            blog = optionalblog.get();
            //判断用户的点赞情况
            List<Vote> votes = blog.getVotes();


            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (principal != null) {
                for (Vote vote : votes) {
                    if (vote.getUser().getUsername().equals(principal.getUsername())) {
                        currentVote = vote;
                        break;
                    }
                }
            }
        }


          model.addAttribute("currentVote", currentVote);
          model.addAttribute("username", username);

          return "/userspace/blog";

    }










}
