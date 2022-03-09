package com.forq.demo.dao;

import com.forq.demo.pojo.Catalog;
import com.forq.demo.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * author long
 *
 * @date 2022/3/9
 * @apiNote
 */
public interface CatalogRespository extends JpaRepository<Catalog ,Long> {
    /**
     * 根据用户查询
     * #@param user
     *
     * @return
     */
    List<Catalog> findByUser(User user);

    /***
     * 根据用户查询
     * @param user
     * @param name
     * @return
     */

    List<Catalog> findByUserAndName(User user,String name);

}
