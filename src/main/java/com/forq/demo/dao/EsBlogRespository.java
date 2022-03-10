package com.forq.demo.dao;

import com.forq.demo.pojo.EsBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * author long
 *
 * @date 2022/3/10
 * @apiNote
 */
public interface EsBlogRespository extends ElasticsearchRepository<EsBlog,String> {
    /**
     * 模糊查询
     * @param title
     * @param summary
     * @param content
     * @param tags
     * @param pageable
     * @return
     */
    Page<EsBlog> findByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(
            String title, String summary, String content, String tags, Pageable pageable
            );
    /**
     * 模糊查询的方法很冗长
     * 但是spring data 会根据方法签名 自动猜测 这个查询语句的意图
     * 从而实现数据查询的目的 这个方法用于模糊查询标题
     *
     * 或者正文
     * 或者是标签包中包含有搜索关键字的数据
     */

    /**
     *
     * @param blogId
     * @return
     *
     */
    EsBlog findByBlogId(Long blogId);

}
