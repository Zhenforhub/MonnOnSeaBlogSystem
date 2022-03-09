package com.forq.demo.service;

import com.forq.demo.pojo.Catalog;
import com.forq.demo.pojo.User;

import java.util.List;
import java.util.Optional;

/**
 * author long
 *
 * @date 2022/3/9
 * @apiNote
 */
public interface CatalogService {
    /**
     * 保存Catalog
     * @param catalog
     * @return
     */
    Catalog saveCatalog(Catalog catalog);

    /**
     * 删除 catalog
     * @param id
     * @return
     */
    void removeCatalog(Long id);
    /**
     * 根据id获取Catalog
     * @param  id
     * @return
     *
     */
    Optional<Catalog> getCatalogById(Long id);
    /**
     * 获取catalog列表
     * @return
     */
    List<Catalog> ListCatalog(User user);

}
