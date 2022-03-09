package com.forq.demo.dao;

import com.forq.demo.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * author long
 *
 * @date 2022/3/5
 * @apiNote
 */

/**
 * 刚才一不小心 把接口写成了类 是在不应该
 * 有关接口的继承 不能马虎
 */
public interface UserRespository  extends JpaRepository<User,Long> {
    /**
     * 我们要做到的是
     *
     * 根据用户姓名分页查询用户列表
     * @param  name
     * @param  pageable
     * @return
     * 采用的是分页查询
     *
     */
    Page<User> findByNameLike(String name, Pageable pageable);
    //这个方法意思是 通过名字的模糊查询 租后得到一个分页
    //这个方法也是应该要实现的

    /**
     * 根据用户账号 查询用户
     * @param  username
     * @return
     */

    User findByUsername(String username);
    //根据用户账号查询的结果要么为空，要么只有一个确定的值


}
