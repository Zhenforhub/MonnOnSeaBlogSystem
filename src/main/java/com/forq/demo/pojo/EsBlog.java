package com.forq.demo.pojo;

/**
 * author long
 *
 * @date 2022/3/10
 * @apiNote
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.lang.annotation.Documented;
import java.sql.Timestamp;

/**
 * 该类用于存储博客的文档
 * Elasticsearch 存储博客的文档
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
@Document(indexName = "blog")
public class EsBlog {
    private static final long serialVersionUID=1L;
    @Id
    private String id;

    @Field(type = FieldType.Long)
    private  Long blogId;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String summary;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type =FieldType.Keyword,index = false)//用户名不做全文检索
    private String username;

    @Field(type =FieldType.Text,index = false)
    private String avatar;
    @Field(type =FieldType.Date,index = false)//凡是不要全文检索的字段 index一律为false,因为默认为true 代表需要全文检索
    private Timestamp createTime;

    @Field(type = FieldType.Integer,index = false)
    private Integer commentSize=0;//评论量

    @Field(type = FieldType.Integer,index = false )
    private  Integer voteSize=0;
    @Field(type = FieldType.Integer,index = false )
    private Integer readSize=0;
    @Field(type =FieldType.Text,index = false)
    private String tags;


    protected EsBlog(){
        /**
         * 因为jpa要求 无参构造函数 设为 protected 防止被直接使用
         */
    }
    @Override
    public String toString(){
        return String.format("EsBlog[blogId=%d, title='%s',summary='%s']",blogId,title,summary);
        /**
         * 利用format方法对字符串进行格式化 参数会以格式化的数据进行编辑返回
         */
    }






}
