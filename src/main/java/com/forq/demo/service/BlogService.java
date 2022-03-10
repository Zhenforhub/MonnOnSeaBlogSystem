package com.forq.demo.service;

/**
 * author long
 *
 * @date 2022/3/8
 * @apiNote
 */

import com.forq.demo.dao.BlogRespository;
import com.forq.demo.pojo.Blog;
import com.forq.demo.pojo.Catalog;
import com.forq.demo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * 针对服务层的接口
 */
public interface BlogService {
    /**
     * 保存Blog
     * @param  blog
     * @return
     */
    Blog saveBlog (Blog blog);

    /**
     * 删除blog
     * @param id
     * @return
     */
    void removeBlog(Long id);

    /**
     * 根据id获取Blog
     * @param id
     * @return
     *
     */

    Optional<Blog> getBlogById(Long id);

    /**
     * 根据用户进行博客名称分页模糊查询(最新)
     * @param user
     * @param title
     * @param pageable
     */
    Page<Blog>  listBlogsByTitleVote(User user, String title, Pageable pageable);
    /**
     * 根据用户进行博客名称分页模糊查询(最热)
     *
     * @param user
     * @param title
     * @param pageable
     * @return
     */

    Page<Blog> listBlogsByTitleVoteAndSort(User user,String title,Pageable pageable);




    /**
     * 阅读量递增
     * @param id
     */
    void readingIncrease(Long id);

    /**
     * 由于增加评论字段 所以有对于评论的操作
     * @param blogId
     * @param commentContent
     */
    Blog createComment(Long blogId,String commentContent);

/**
 *
 * 删除评论
 * @param  blogId
 * @param  commentId
 * @return
 */
   void removeComment(Long blogId,Long commentId);

    /**
     * 针对博客点赞所以要增加两个方法
     */
    /**
     * 点赞
     * @param blogId
     * @return
     */
    Blog createVote(Long blogId);

    /**
     *
     * @param blogId
     * @param voteId
     */
    void removeVote(Long blogId,Long voteId);

    /**
     * 因为要进行分类的管理
     * 所以增加接口
     */
    /**
     * 根据分类进行查询
     * @param catalog
     * @param pageable
     * @return
     */
    Page<Blog> listBlogByCatalog(Catalog catalog,Pageable pageable);



}
