package com.forq.demo.service;

import com.forq.demo.pojo.Authority;

import java.util.Optional;

/**
 * author long
 *
 * @date 2022/3/6
 * @apiNote
 */
public interface AuthorityService {

    /*8
   根据id 查询Authority
   @param id
   @return

     */
    Optional<Authority> getAuthorityById(Long id);

}
