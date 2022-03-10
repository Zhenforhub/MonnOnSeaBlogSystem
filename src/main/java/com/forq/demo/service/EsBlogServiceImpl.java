package com.forq.demo.service;

import com.forq.demo.dao.EsBlogRespository;
import com.forq.demo.pojo.EsBlog;
import com.forq.demo.pojo.User;
import com.forq.demo.vo.TagVo;
import org.apache.lucene.queries.SearchAfterSortedDocQuery;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndicesOptions;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * author long
 *
 * @date 2022/3/10
 * @apiNote
 */
@Service
public class EsBlogServiceImpl implements EsBlogService{

    @Autowired
    private EsBlogRespository esBlogRespository;

    @Resource
    @SuppressWarnings(value = "ElasticsearchTemplate")
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private UserService userService;


    private static final  Pageable TOP_5_PAGEABEL=PageRequest.of(0,5);

    private static  final String EMPTY_KEYWORD="";


    @Override
    public void removeBlog(String id) {

       esBlogRespository.deleteById(id);
    }

    @Override
    public EsBlog updateEsBlog(EsBlog esBlog) {

        return esBlogRespository.save(esBlog);
    }

    @Override
    public EsBlog getEsBlogById(String  id) {
        return esBlogRespository.findById(id).get();
    }

    @Override
    public EsBlog getEsBlogByBlogId(Long blogId) {
        return esBlogRespository.findByBlogId(blogId);
    }

    @Override
    public Page<EsBlog> listNewsEsBlogs(String keyword, Pageable pageable) {
        Page<EsBlog> pages=null;
        List orders;
        Sort sort=Sort.by(Sort.Direction.DESC,"createTime");
        /**
         * Sort已经不能再实例化了 所以被报这种错误
         */
        if(pageable.getSort().isUnsorted()){
            pageable=PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(),sort);
            /**
             * 它的意思是如果pageable中的sort是无序的 则让pageable出现有序的状态
             */
        }
        pages=esBlogRespository.findByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(
                keyword,keyword,keyword,keyword,pageable);
        return  pages;


    }

    @Override
    public Page<EsBlog> listHotestEsBlogs(String keyword, Pageable pageable) {
        Sort sort=Sort.by(Sort.Direction.DESC,"readSize","commentSize","voteSize","createTime");
        if(pageable.getSort().isSorted()){
            pageable=PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(),sort);
        }
        return esBlogRespository.findByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(
                keyword,keyword,keyword,keyword,pageable
        );

    }

    @Override
    public Page<EsBlog> listEsBlogs(Pageable pageable) {

        return esBlogRespository.findAll(pageable);
        /**
         * 按照分页条件对所有的数据进行分页查询返回页面结果
         */
    }

    @Override
    public List<EsBlog> listTop5NewestEsBlogs() {
        Page<EsBlog> page=this.listNewsEsBlogs(EMPTY_KEYWORD,TOP_5_PAGEABEL);
        /**
         * 通过最热的查询方式 返回新前五
         */
        return page.getContent();
    }

    @Override
    public List<EsBlog> listTop5HotestEsBlogs() {
        Page<EsBlog> page=this.listHotestEsBlogs(EMPTY_KEYWORD,TOP_5_PAGEABEL);
        return page.getContent();
    }

    @Override
    public List<TagVo> listTop30Tags() {

        List<TagVo> list=new ArrayList<>();

        //查询条件
        NativeSearchQuery searchQuery= new NativeSearchQueryBuilder()
                /***
                 * 创建条件构造器
                 */
                .withQuery(new MatchAllQueryBuilder())
                /**
                 * 匹配所有的查询构造器
                 */
                .withSearchType(SearchType.QUERY_THEN_FETCH)

        /**
         加入要查找的类型 这个类型是 查询然后fetch
         */
                .withIds("blog")
        /**
         * 找到要查找的索引
         */
        .withFields("blog")
                .withAggregations().build();
        /**;
         * 卡在复杂的查询上面
         * 条件太多
         * 而且对于如何用工具进行查询不懂
          */
        /**
         * 要想查询好 就先构造好查询的条件
         * 这是查询所必须的
         * 构造查询条件 有条件查询构造器
         * elasticsearch 的查询条件构造器 就是 nativaSearchQueryBuider
         * 构造好了以后就是 填充条件
         * 那么填充哪些条件呢？
         * 首先是匹配 匹配 哪些内容呢？
         * 匹配好条件 在匹配好查询的类型
         * 佩佩好搜索的类型在此基础上再匹配索引 和 类型
         * 再对匹配好的数据进行聚合操作
         * 聚合的对象是users  匹配的字段时username
         * 然后order
          */
        return null;

        /**
         * 突然技术老化，我该怎么办
         *
         */
    }

    @Override
    public List<User> listTop12Users() {
        return null;
    }
}
