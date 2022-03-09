package com.waylau.spring.boot.fileserver.service;

import com.waylau.spring.boot.fileserver.dao.FilesRespository;
import com.waylau.spring.boot.fileserver.domain.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * author long
 *
 * @date 2022/3/6
 * @apiNote
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FilesRespository filesRespository;

    @Override
    public File saveFile(File file) {

        return filesRespository.save(file);

    }

    @Override
    public void removeFile(String id) {
        filesRespository.deleteById(id);
    }

    @Override
    public Optional<File> getFileById(String id) {
        return filesRespository.findById(id);
    }

    /**
     * 分页查询，按上传时间降序
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public List<File> listFileByPage(int pageIndex, int pageSize) {

        Page<File> page = null;
        /**
         * 提前准备好分页条件容器
         */
        List<File> list = null;
        /**
         * 提前准备好列表 准备承接 分页的数据
         */
        Sort sort = null;

        //   sort=new Sort(Sort.Direction.DESC,"uploadDate"); 这段有点问题 暂且注释掉
        /**
         * 我有点没看懂是什么意思
         * 在开发方面 无法理解
         * 这啥意思啊
         */
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        /**
         * Pageable 的作用是什么？
         * 首先 根据 后面的文件持久方法 findAll来看
         * 当传入 Pageanle以后 根据PageRequest。of方法传入好了 page索引 和page尺寸 还有排序方案
         * Pageable构成了查询的必要条件 从内容上看有点像条件构造器
         * 按照这种条件执行的查询结果是一个元素为File的page容器对象
         * 然后通过Page 容器的getcontent()方法
         * 最终就得到了想要的List
         * 并且返回list
         * 得到最终的结果
         * 最终的结果就是 按照 上传时间进行降序排列的list的列表
         * 并且是分页后的查询结果
         */

        page = filesRespository.findAll(pageable);

        list = page.getContent();

        return list;

        /**
         *
         * 上了年纪的代码
         * 有时候还看不太懂
         * 不知道该怎么入手
         */
    }
}
