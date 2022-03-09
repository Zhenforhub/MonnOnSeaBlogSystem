package com.forq.demo.service;

import com.forq.demo.dao.BlogRespository;
import com.forq.demo.pojo.Blog;
import com.forq.demo.pojo.Comment;
import com.forq.demo.pojo.User;
import com.forq.demo.pojo.Vote;
import com.sun.crypto.provider.BlowfishKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.RollbackException;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * author long
 *
 * @date 2022/3/8
 * @apiNote
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRespository blogRespository;
    @Autowired
    private  VoteService voteService;

    @Transactional
    /**
     * 加入Transactional
     * 就是表明该方法就是一个事务
     * 如果事务执行异常就会将事务进行回滚
     * 这样就保证的事务执行的原子性
     */
    @Override
    public Blog saveBlog(Blog blog) {



        Blog returnBlog=blogRespository.save(blog);
        return returnBlog;
        /**
         * 博客保存好以后 博客的数据还是继续给到用户
         * 所以要返回
         */
    }

    @Transactional(rollbackOn = RuntimeException.class)
    /**
     * 当发生运行时异常时回滚
     */
    @Override
    public void removeBlog(Long id) {
        blogRespository.deleteById(id);

        /**
         * 方法无需返回
         */
    }

    @Transactional
    @Override
    public Optional<Blog> getBlogById(Long id) {

        return blogRespository.findById(id);
        /**
         * 没有多余的代码 直接返回就可以了
         * 仓库接口就是返回optional
         */
    }
    @Transactional
    @Override
    public Page<Blog> listBlogsByTitleVote(User user, String title, Pageable pageable) {

        /**
         * 我首先要搞清楚一点 仓库有两个接口是我自己写的 所以他们需要实现类
         * 但是作为业务层 我只需要调用 仓库的接口就可以了
         * 具体仓库需要怎么做还另说
         */
        /**
         * 具体的模糊查询办法
         */
        title="%"+title+"%";//构造模糊查询的条件

        String tags=title;
        /**
         * 将主题变成标签
         */
        Page<Blog> blogs=blogRespository
                .findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc
                        (title,user,tags,user, (java.awt.print.Pageable) pageable);
        return blogs;
    }
    @Transactional
    @Override
    public Page<Blog> listBlogsByTitleVoteAndSort(User user, String title, Pageable pageable) {
        title="%"+title+"%";
        Page<Blog> blogs=blogRespository.findByUserAndTtileLike(user, title, (java.awt.print.Pageable) pageable);
        /**
         * 我比较想知道pageable是怎么用的 接口的条件是怎么样的
         */
        return blogs;
        /**
         * blogs就是我们要用到的页面
         */
    }

    @Transactional
    @Override
    public void readingIncrease(Long id) {

        Optional<Blog> blog=blogRespository.findById(id);

        Blog blogNew=null;

        if(blog.isPresent()){
            blogNew=blog.get();
            blogNew.setReadSize(blogNew.getReadSize()+1);
            //在原有的阅读量上面增加1
            this.saveBlog(blogNew);
        }

    }

    @Override
    public Blog createComment(Long blogId, String commentContent) {
        Optional<Blog> optionalBlog=blogRespository.findById(blogId);
        Blog originalBlog =null;
        if(optionalBlog.isPresent()){
            originalBlog=optionalBlog.get();
            User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Comment comment=new Comment(null,commentContent,user ,null );
            originalBlog.addComment(comment);
        }
        return this.saveBlog(originalBlog);
    }

    @Override
    public void removeComment(Long blogId, Long commentId) {

         Optional<Blog> optionalBlog=blogRespository.findById(blogId);

             Blog originalBlog=null;

         if(optionalBlog.isPresent()){
             originalBlog =optionalBlog.get();
             originalBlog.removeComment(commentId);
             this.saveBlog(originalBlog);
         }

    }

    @Override
    public Blog createVote(Long blogId) {
        Optional<Blog> optionalBlog=blogRespository.findById(blogId);
        Blog originalBlog=null;
        if(optionalBlog.isPresent()){
            originalBlog=optionalBlog.get();
            User user=(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Vote vote=new Vote(null, user,null);
            boolean isExist=originalBlog.addVote(vote);

            if(!isExist){
                throw  new IllegalArgumentException("该用户已经点过赞了");
                /**
                 * 这个主动抛出异常的的行为令人不解
                 * 而且也没有捕获，这意味着一旦失败就会把异常交给上面
                 */
            }
        }
        return this.saveBlog(originalBlog);

        /**
         * 原来我在创建点赞的同时就已经把这个博客进行保存了，也将用户为空的
         * 所以 在底层能解决的问题 就绝不要交给上层
         * 在控制层 只需要调用业务层的点赞方法就够了 不需要给自己加戏
         * 问题的关键就在于将所有的相关的改动就交给底层去做
         * 这个底层就是业务层
         */

    }

    @Override
    public void removeVote(Long blogId, Long voteId) {
        /**
         * 要懂就要懂与之相关联的类
         * 从单一类到关联类 最后这三个状态之间相互纠结
         * 最终实现各自的状态的统一
         */
        Optional<Blog> optionalBlog=blogRespository.findById(blogId);

        Blog originalBlog=null;
        if(optionalBlog.isPresent()){

            originalBlog=optionalBlog.get();

            originalBlog.removeVote(voteId);

            this.saveBlog(originalBlog);
        }

    }
}
