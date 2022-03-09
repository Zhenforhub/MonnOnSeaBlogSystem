package com.forq.demo.service;

import com.forq.demo.dao.UserRespository;
import com.forq.demo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * author long
 *
 * @date 2022/3/5
 * @apiNote
 */
@Service

public class UserServiceImpl implements UserService , UserDetailsService {

    @Autowired
    private UserRespository userRespository;

    @Transactional//为什么要在这加transactional配置注解
    @Override
    public User svaeOrUpdateUser(User user) {

        return  userRespository.save(user);

        //不仅注册会用到，而且连更新也会用到
    }

    @Override
    public User registerUser(User user) {
        return userRespository.save(user);
    }

    @Override
    public void removeUser(Long id) {
       userRespository.deleteById(id);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRespository.findById(id);
    }

    /**
     *
     * 这个optional
     * 不太懂什么意思
     */

    @Override
    public Page<User> listUsersByNameLike(String name, Pageable pageable) {

        //关于这模糊查询要注意

        name="%"+name+"%";//前后两端加百分号 对模糊查询比较合理

        Page<User> users=userRespository.findByNameLike(name, pageable);
        /**
         * 将条件从封装好的模糊查询方法去查
         * 就可以得到分页查询的结果
         */

        return  users;
        //最终返回用户查询的分页
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /**
         * 为什么要继承通过用户名来加载用户
         *
         *
         */
        return userRespository.findByUsername(username);

    }
}
