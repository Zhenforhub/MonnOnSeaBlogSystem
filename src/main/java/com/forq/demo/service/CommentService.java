package com.forq.demo.service;

import com.forq.demo.pojo.Comment;

import java.util.Optional;

/**
 * author long
 *
 * @date 2022/3/8
 * @apiNote
 */
public interface CommentService {
    /**
     * 根据id获取评论
     * @param id
     * @return
     */
    Optional<Comment> getCommentById(Long id);

    /**
     * 删除评论
     * @param id
     * @return
     */
    void removeComment(Long id);

}
