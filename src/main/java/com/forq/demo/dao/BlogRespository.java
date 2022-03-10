package com.forq.demo.dao;

import com.forq.demo.pojo.Blog;
import com.forq.demo.pojo.Catalog;
import com.forq.demo.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

//import java.awt.print.Pageable;
import java.util.List;

/**
 * author long
 *
 * @date 2022/3/7
 * @apiNote
 */

/**
 * 创建Blog 仓库接口，用于博客的分页查询
 */
public interface BlogRespository  extends JpaRepository<Blog,Long> {

    /**
     * 根据用户名，博客标题 分页查询博客列表
     * @param user
     * @param title
     * @param pageable
     * @return
     *
     */
    /**
     * 不管怎么说经过分页查询之后的结果
     * 首先是用户名 唯一绑定的一个用户
     * 博客的标题有很多
     * 返回值最后是list 但是分页查询的结果 分页查询的尺寸为多少 pageSize
     */
    Page<Blog> findByUserAndTtileLike(User user, String title, Pageable pageable);
    /**
     * 最终拿到的结果是blog的分页
     */
    /**
     * 根据用户名。博客查询博客列表(时间逆序)
     * @param title
     * @param user
     * @param tages
     * @param user2
     * @param pageable
     * @return
     */
    Page<Blog> findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(String title,User user,String tages,User user2,Pageable pageable);
    /**
     * 根据用户名和博客来查询这个得到的分页就太大了
     * 而且还要时间逆序
     */

    /**
     * 用于管理分类的增加的方法
     */
    /**
     * @param  catalog
     * @param pageable
     * @return
     */
    Page<Blog> findByCatalog(Catalog catalog, Pageable pageable);




}
