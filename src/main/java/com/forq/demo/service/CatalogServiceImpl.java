package com.forq.demo.service;

/**
 * author long
 *
 * @date 2022/3/9
 * @apiNote
 */

import com.forq.demo.pojo.Catalog;
import com.forq.demo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CatalogServiceImpl implements CatalogService {
    
    @Autowired
   private CatalogService catalogService;
    @Override
    public Catalog saveCatalog(Catalog catalog) {
        return null;
    }

    @Override
    public void removeCatalog(Long id) {

    }

    @Override
    public Optional<Catalog> getCatalogById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Catalog> ListCatalog(User user) {
        return null;
    }
}
