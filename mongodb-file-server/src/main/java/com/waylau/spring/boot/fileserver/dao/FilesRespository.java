package com.waylau.spring.boot.fileserver.dao;

import com.waylau.spring.boot.fileserver.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * author long
 *
 * @date 2022/3/6
 * @apiNote
 */

public interface FilesRespository extends MongoRepository<File,String> {

}
