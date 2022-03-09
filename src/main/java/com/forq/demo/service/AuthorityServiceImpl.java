package com.forq.demo.service;

import com.forq.demo.dao.AuthorityRespository;
import com.forq.demo.pojo.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * author long
 *
 * @date 2022/3/6
 * @apiNote
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {
    @Autowired
    AuthorityRespository authorityRespository;
    @Override
    public Optional<Authority> getAuthorityById(Long id) {

      return    authorityRespository.findById(id);
        /**
         * 通过 利用持久层的方法 id 参数查询到想要的结果最后返回
         */


    }
}
