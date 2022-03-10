package com.forq.demo.service;

/**
 * author long
 *
 * @date 2022/3/9
 * @apiNote
 */

import com.forq.demo.dao.CatalogRespository;
import com.forq.demo.pojo.Catalog;
import com.forq.demo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CatalogServiceImpl implements CatalogService {

    @Autowired
   private CatalogRespository catalogRespository;
    /**
     * 如果发生非法参数异常
     * 但是如果不向上报就会 用户就得不到那么多的信息，所以抛出异常，然后向上报
     *
     */
    @Override
    public Catalog saveCatalog(Catalog catalog) {
        /**
         * 首先要判断重复
         *
         */
        List<Catalog> list=catalogRespository.findByUserAndName(catalog.getUser(), catalog.getName());
        if(list!=null&&list.size()>0){
            throw  new IllegalArgumentException("该分类已经存在");
            /**
             * 对于一些 非法的参数 但是不能保证用户不传这样的参数
             * 这时候抛出异常表示无法解决，很有必要
             */
            /**
             * 指出传入了非法的参数，导致的异常状态
             */
            /**
             * 业务层执行了保存分类
             * 保存分类 首先看是否已经建立了分类
             * 如果已经建立了该分类，对于用户来说就不能及时的标识
             * 所以采用分类名和 用户名去认证该分类
             * 但是显然这种分类是不可细分的
             */
        }
        return catalogRespository.save(catalog);
    }

    @Override
    public void removeCatalog(Long id) {
      catalogRespository.deleteById(id);
    }

    @Override
    public Optional<Catalog> getCatalogById(Long id) {
        return catalogRespository.findById(id);
        /**
         * 没想到find和get是两回事
         */
    }

    @Override
    public List<Catalog> ListCatalog(User user) {

        return  catalogRespository.findByUser(user);
        /**
         * 为什么有findbyuser
         * 因为 已经关联了外键
         * 通过该用户查询到了所有的分类
         */
    }

}
