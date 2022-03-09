package com.forq.demo.dao;

import com.forq.demo.pojo.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * author long
 *
 * @date 2022/3/8
 * @apiNote
 */
public interface VoteRespository extends JpaRepository<Vote ,Long> {

}
