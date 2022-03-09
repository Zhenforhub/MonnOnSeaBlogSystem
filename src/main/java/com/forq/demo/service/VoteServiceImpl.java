package com.forq.demo.service;

import com.forq.demo.dao.VoteRespository;
import com.forq.demo.pojo.Vote;
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
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteRespository voteRespository;

    @Override
    public Optional<Vote> getVoteById(Long id) {
        return voteRespository.findById(id);
    }

    @Override
    public void removeVote(Long id) {

        voteRespository.deleteById(id);

    }
}
