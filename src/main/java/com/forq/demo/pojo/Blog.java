package com.forq.demo.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.experimental.Accessors;
import org.assertj.core.internal.bytebuddy.implementation.bind.MethodDelegationBinder;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;


import javax.annotation.processing.Processor;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.xml.soap.Name;
import java.io.Serializable;
import java.security.Timestamp;
import java.util.List;

/**
 * author long
 *
 * @date 2022/3/7
 * @apiNote
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
@Entity
public class Blog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//依然是采用自增长策略
    private Long id;//用户的唯一标识索引

    @NotEmpty(message="标题不能为空")
    @Size(min=2,max=50)
    @Column(nullable = false ,length = 50)//映射为字段，值不能为空
    private String title;

    @NotEmpty(message="摘要不能为空")
    @Size(min=2,max=300)
    @Column(nullable = false)
    private String summary;

    @Lob//表示大对象 ，映射MySql的LONG Text类型
    @Basic(fetch=FetchType.LAZY)//执行懒加载策略
    @NotEmpty(message = "内容不能为空")
    @Size(min=2)
    @Column(nullable = false)
    private String content;

    @Lob
    @Basic(fetch=FetchType.LAZY)
    @NotEmpty(message = "内容不能为空")
    @Size(min=2)
    @Column(nullable = false )
    private String htmlContent;//将markdown 变成 html

    @OneToOne(cascade=CascadeType.DETACH,fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable=false)//映射为字段 ，值不能为空
    @org.hibernate.annotations.CreationTimestamp//数据库自动创建时间
    private Timestamp createTime;

    @Column(name="readSize")
    /**
     * 之所以会出现这种红线，主要是没有配置数据源
     * 不过没有配置数据源倒不是什么大事
     */
    private Integer readSize=0; //访问量 ，阅读量



    @Column(name="voteSize")
    private Integer voteSize=0;//点赞量

    @Column(name="tags",length = 100)
    private String tags;//标签
    /**
     *标签 难道只有一个？
     */

    /**修改blog类来建立起blog 与comment之间的关系
     *
     *

     */
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(
            name="blog_comment"
            ,joinColumns=@JoinColumn(name="blog_id",referencedColumnName = "id")
            ,inverseJoinColumns = @JoinColumn(name = "comment_id",referencedColumnName = "id")
    )
    private List<Comment> comments;

    @NotEmpty(message = "评论数不能为空")
    @Size(min = 0)
    @Column(nullable = false)
    private Integer commentSize;

    /**
     * 为了建立与博客之间的联系
     *
     * 修改博客类
     */
    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.LAZY)
    @JoinTable(name="blog_vote"
            , joinColumns = @JoinColumn(name="blog_id",referencedColumnName ="id" )
         ,inverseJoinColumns = @JoinColumn(name = "vote_id",referencedColumnName ="id"))

    /**
     * 有关表与表之间的字段连接问题
     * 要搞清楚
     * 特别是这些个注解
     */
    private List<Vote> votes;

    /**
     * 增加了 博客的分类
     * 一个博客只能对应一个分类
     * 但是一个分类有多个博客
     */

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name="catalog_id")
    private Catalog catalog;




    public void setVotes(List<Vote> votes){
        this.votes=votes;

        this.voteSize=this.votes.size();
    }

    /**
     *实现点赞功能
     * @param vote
     * @return
     */
    /**
     * 如果点赞没有用户
     * 只有博客
     * 那被点赞的播客
     * 如果将来要取消点赞
     * 将哪个点赞取消呢？
     *
     * @param vote
     * @return
     */
    public boolean addVote(Vote vote){
        boolean isExist=true;
        /**
         * 我的理解是这样的
         * 当博客中有该赞时，点赞就会失效
         * 如果没有该赞，点赞才会成功
         */
        for(int index=0;index<this.votes.size();index++){
            if(this.votes.get(index).getUser().getId().equals(vote.getUser().getId())){
                isExist=false;
                break;
            }

        }
        if(isExist){
            this.votes.add(vote);
            this.voteSize=this.votes.size();
        }
        return isExist;
        /**
         * 返回点赞的成功和失败
         * 既然在点赞中已经有vote了  那么点赞应该是失败才对 这没道理啊
         */
    }

    /**
     * 接下来是取消点赞
     * @param  voteId
     */
    public void removeVote(Long voteId){

        for (Vote v:votes) {

           if(v.getUser().getId().equals(voteId)){

               votes.remove(voteId);

               this.voteSize=this.votes.size();
               break;
           }


        }

    }






    protected Blog(){

    }


    public List<Comment> getComments(){

        return comments;
    }
    public void setComments(List<Comment> comments){

        this.comments=comments;
        this.commentSize=comments.size();
    }


    /**
     * 让无参构造方法受保护
     */
 public Timestamp getCreatedTime(){
     return createTime;
 }

 public String getHtmlContent(){
     return htmlContent;
 }

    /**
     * 作为新增的关于评论的方法
     * 删除评论
     * @param commentId
     *
     */
    public void removeComment(Long commentId){
        for (Comment c:comments) {
            if( c.getId().equals(commentId)){
                comments.remove(c);
                break;
            }
        }

        this.commentSize=this.comments.size();

        }
        public void addComment(Comment comment){
           this.comments.add(comment);
           this.commentSize++;
        }





 public void setContent(String content) {
     this.content=content;

     /**
      * 涉及将markdown内容转化为HTML格式
      * 因为我没有采用书上给出的 markdown转 HTML格式的依赖
      * 而是用的另外一种依赖
      * 所以应该用另外一种依赖的markdown 转html格式的工具
      */
     Parser parser = Parser.builder().build();
     /**
      * 这就是我要找的 markdown解析器
      */
     Node document = parser.parse(content);

     HtmlRenderer renderer = HtmlRenderer.builder().build();

     this.htmlContent=renderer.render(document);
     /**
      * 这就是想要的表
      */


 }








}
