package com.forq.demo.controller;

import com.forq.demo.pojo.Blog;
import com.forq.demo.pojo.User;
import com.forq.demo.pojo.Vote;
import com.forq.demo.service.BlogService;
import com.forq.demo.service.VoteService;
import com.forq.demo.util.ConstraintViolationExceptionHandler;
import com.forq.demo.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

/**
 * author long
 *
 * @date 2022/3/8
 * @apiNote
 */
@RestController
@RequestMapping("/votes")
public class VoteController {
    @Autowired
    private VoteService voteService;

    @Autowired
    private BlogService blogService;

    /**
     * 发表点赞
     * @param blogId
     * @param
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    //指定有操作权限的用户才有操作方法
    public ResponseEntity<Response> createVote(Long blogId){
     //TODO
        try{
            /**
             * 通过博客的id 可以由发表一个点赞
             * 因为博客有点赞 处
             * 有点赞的 数
             */
            /**
             * 关联特定的人
             * 管理与权限的操作知道有哪个人
             */
            /**
             * 人对于这个博客的点赞 最终加到 博客的点赞
             */
            blogService.createVote(blogId);
            /***
             * 因为无论如何 业务层都已经将事情做好了
             * 我们只需要按照特定的方法去调用就是了
             */

        }catch (ConstraintViolationException e){
            return ResponseEntity
                    .ok()
                    .body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }catch (Exception e){
            return ResponseEntity
                    .ok()
                    .body(new Response(false,e.getMessage()));
        }


        return ResponseEntity
                .ok()
                .body(new Response(true,"点赞成功!",null));
    }
    /**
     * 删除点赞
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> delete(@PathVariable("id") Long id,Long blogId){
        /**
         * 这个id就是指vote的id
         * 而博客去删除id 用的方法 不管如何
         * 当我们加入数据的时候要考虑约束异常
         * 删除数据的时候不用删除记录 既包括 vote 也包括博客
         * 用户自己删除
         */
        /**
         * 用户只给了id
         * 然而博客自己有博客的id
         * 也就是说 我的id
         */
        Optional<Vote> optionalVote=voteService.getVoteById(id);

         User user=null;
         boolean isOwner=false;
        if(optionalVote.isPresent()){
             user=optionalVote.get().getUser();
        }else {
            return ResponseEntity
                    .ok()
                    .body(new Response(false,"没有该赞"));
        }

        /**
         * 我们必须进第二
         */
        if(SecurityContextHolder.getContext().getAuthentication()!=null
                &&SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
        &&!SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")
        ){
            User principal=(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
             if(principal!=null&&user.getUsername().equals(principal.getUsername())) {
                 isOwner = true;
             }
        }

        if(isOwner){
            try{
                    voteService.removeVote(id);
                    blogService.removeVote(blogId,id);

            }catch (ConstraintViolationException e){
                return ResponseEntity
                        .ok()
                        .body(new Response(false,ConstraintViolationExceptionHandler.getMessage(e)));
            }catch (Exception e){
                return  ResponseEntity
                        .ok()
                        .body(new Response(false,e.getMessage()));
            }


            return ResponseEntity.ok()
                    .body(new Response(true,"取消点赞成功!",null));
        }else {
            return ResponseEntity
                    .ok()
                    .body(new Response(false,"没有操作权限",null));
        }

    }





}
