package com.forq.demo.service;

import com.forq.demo.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * author long
 *
 * @date 2022/3/5
 * @apiNote
 */
public interface UserService {
    /**
     * 新增 。编辑 ，保存用户
     * @param  user
     * @return
     */
    User svaeOrUpdateUser(User user);

    /**
     * 注册用户
     * @param  user
     * @return
     *
     */
    User registerUser(User user);

    /**
     * 删除用户
     * @param id
     *
     */
    void removeUser(Long id);

    /**
     * 根据id 获取用户
     * @param  id
     * @return
     */
    Optional<User> getUserById(Long id);

    /**
     * 根据 用户名 进行分页模糊查询 这个也是重中之重
     * @param  name
     * @param  pageable
     * @return
     */
    Page<User> listUsersByNameLike(String name , Pageable pageable);

}
