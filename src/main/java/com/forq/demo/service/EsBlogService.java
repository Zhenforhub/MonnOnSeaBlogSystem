package com.forq.demo.service;

import com.forq.demo.pojo.EsBlog;
import com.forq.demo.pojo.User;
import com.forq.demo.vo.TagVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * author long
 *
 * @date 2022/3/10
 * @apiNote
 */
public interface EsBlogService{

    /**
     * 删除Esblog
     * @param id
     *
     */
    void removeBlog(String id);

    /**
     * 更新Esblog
     * @param esBlog
     * @return
     */
    EsBlog updateEsBlog(EsBlog esBlog);

    /**
     * 跟据id 来获取EsBlog
     * @param id
     * @return
     */
    EsBlog getEsBlogById(String id);

    /**
     * 跟据博客的id 来获取博客
     * @param blogId
     * @return
     */
    EsBlog getEsBlogByBlogId(Long blogId);

    /**
     * 跟据分页条件 按照最新的方式进行查询
     * @param keyword
     * @param pageable
     * @return
     */
    Page<EsBlog> listNewsEsBlogs(String keyword, Pageable pageable);

    /**
     * 博客列表 分页
     * @param keyword
     * @param pageable
     * @return
     */
    Page<EsBlog> listHotestEsBlogs(String keyword,Pageable pageable);

    /**
     * 博客列表的分页
     * 很显然是针对所有的博客列表
     * @param pageable
     * @return
     */

    Page<EsBlog> listEsBlogs(Pageable pageable);


    /**
     * 最新前五的查询
     *
     *
     */
    List<EsBlog> listTop5NewestEsBlogs();

    /**
     * 最热前5查询
     */

    List<EsBlog> listTop5HotestEsBlogs();

    /**
     * 最热前30标签
     * @return
     */
    List<TagVo> listTop30Tags();

    /**
     * 最热前12用户
     * @return
     */
    List<User> listTop12Users();

}
