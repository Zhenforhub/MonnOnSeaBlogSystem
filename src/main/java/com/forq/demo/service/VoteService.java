package com.forq.demo.service;

import com.forq.demo.pojo.Vote;

import java.util.Optional;

/**
 * author long
 *
 * @date 2022/3/8
 * @apiNote
 */
public interface VoteService {

    /**
     * 根据id 获取vote
     * @param id
     * @return
     */
    Optional<Vote> getVoteById(Long id);

    /**
     * 删除vote
     * @param id
     * @return
     */
    void removeVote(Long id);
}
