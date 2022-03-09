package com.waylau.spring.boot.fileserver.service;

import com.waylau.spring.boot.fileserver.domain.File;

import java.util.List;
import java.util.Optional;

/**
 * author long
 *
 * @date 2022/3/6
 * @apiNote
 */
public interface FileService {
    /**
     *  FileService 定义了对文件的CURD操作，其中查询文件接口采用的是分页处理
     *
     *  提高查询性能
     */
    /**
     * 保存文件
     * @param file
     * @return
     */
    File saveFile(File file);
    /**
     * 删除文件
     * @param id
     * @return void
     */
    void removeFile(String id);

    /**
     * 根据 id获取文件
     * @param  id
     * @return
     */
    Optional<File> getFileById(String id);

    /**
     * 分页查询，按上传时间降序
     * @param pageIndex
     * @param  pageSize
     * @return
     *
     */
    List<File> listFileByPage(int pageIndex,int pageSize);


}
