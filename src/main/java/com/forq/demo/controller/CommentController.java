package com.forq.demo.controller;

import com.forq.demo.pojo.Blog;
import com.forq.demo.pojo.Comment;
import com.forq.demo.pojo.User;
import com.forq.demo.service.BlogService;
import com.forq.demo.service.CommentService;
import com.forq.demo.util.ConstraintViolationExceptionHandler;
import com.forq.demo.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

/**
 * author long
 *
 * @date 2022/3/8
 * @apiNote
 */
@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    /**
     * 获取评论列表
     *
     * @param blogId
     * @param model
     * @return
     */
    @GetMapping("/{blogId}")
    public String listComments(@RequestParam(value = "blogId", required = true) Long blogId
            , Model model) {
        Optional<Blog> optionalBlog = blogService.getBlogById(blogId);
        List<Comment> comments = null;
        if (optionalBlog.isPresent()) {
            comments = optionalBlog.get().getComments();

        }
        //判断用户是否为评论的所有者
        String commentOwner = "";
        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            User principal=(User) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();
            if(principal!=null){
                commentOwner=principal.getUsername();
            }
        }

        model.addAttribute("commentOwner",commentOwner);
        model.addAttribute("comments",comments);

        return  "/userspace/blog :: #mainContainerRepleace";


    }
    /**
     * 发表评论
     * @param blogId
     * @param commentContent
     * @return
     */
    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    //指定的角色权限才能操作方法
    public ResponseEntity<Response> createComment(Long blogId, String commentContent){

        try {
            blogService.createComment(blogId, commentContent);
        }
         catch (ConstraintViolationException e){
            return  ResponseEntity
                    .ok()
                    .body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));

         }catch (Exception e){
            return ResponseEntity
                    .ok()
                    .body(new Response(false,e.getMessage()));
        }
        return  ResponseEntity
                .ok()
                .body(new Response(true,"处理成功",null));
    }

    /**
     * 删除评论
     * @param id
     * @param blogId
     * @return
     */
   @GetMapping("/{id}")
   @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
   /**
    * 关于该注解的认识
    * 就是 当注解PreAuthorize时，只有相关的角色可以进行操作 ROLE_USER和 ROLE_ADMIN
    * 否则就不能进行操作
    */
    public ResponseEntity<Response> delete(@PathVariable("id") Long id,Long blogId){
      //TODO
       boolean isOwner=false;
       Optional<Comment> optionalComment=commentService.getCommentById(id);
       User user=null;
       if(optionalComment.isPresent()){
           user=optionalComment.get().getUser();

       }else {
           return ResponseEntity.ok()
                   .body(new Response(false,"不存在该评论!"));

       }
       //判断操作用户是否为评论的所有者
       if(SecurityContextHolder.getContext().getAuthentication()!=null
               &&SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
               &&!SecurityContextHolder.getContext().getAuthentication().getPrincipal()
               .toString().equals("anonymousUser")

       /**
        * 通过连续三个布尔类型的值发现 用户是否为评论的拥有者
        * 首先是SecurityContextHolder 很明显是Spring security的类
        * 拿到相关的内容 getContext()
        * 拿到相关的权限 getAuthentication 返回布尔值
        * 然后从getAuthentication中拿到 是否是 isAuthenticated 返回布尔值
        * 最后拿到 该用户的用户
        */
       ) {
           User principal = (User) SecurityContextHolder
                   .getContext()
                   .getAuthentication()
                   .getPrincipal();
           if (principal != null && user.getUsername().equals(principal.getUsername())) {
               isOwner = true;
           }
       }
       if(!isOwner){
           return ResponseEntity
                   .ok()
                   .body(new Response(false, "没有操作权限"));
       }

       try {
           blogService.removeComment(blogId,id);
           commentService.removeComment(id);
       }catch (ConstraintViolationException e){
           return ResponseEntity.ok()
                   .body(new Response(false,ConstraintViolationExceptionHandler.getMessage(e)));

       }catch (Exception e){
           return ResponseEntity.ok()
                   .body(new Response(false,e.getMessage()));
       }

       return ResponseEntity.ok()
               .body(new Response(true,"处理成功!",null));

   }


}



