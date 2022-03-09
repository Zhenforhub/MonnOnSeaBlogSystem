package com.forq.demo.service;

import com.forq.demo.dao.CommentRespository;
import com.forq.demo.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * author long
 *
 * @date 2022/3/8
 * @apiNote
 */
@Service
public class CommentServiceImpl  implements CommentService{
    @Autowired
    private CommentRespository commentRespository;

    @Override
    public Optional<Comment> getCommentById(Long id) {

        return commentRespository.findById(id);
    }

    @Override
    public void removeComment(Long id) {

        commentRespository.deleteById(id);
    }
    /**
     * 没有足够的项目千锤百炼自己
     *
     */
}
