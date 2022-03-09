package com.forq.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * author long
 *
 * @date 2022/3/8
 * @apiNote
 */
@Entity
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class Comment implements Serializable {
    private static final long serialVersionUID =1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;//用户的唯一标识

    @NotEmpty(message = "评论内容不能为空")
    @Size(min = 2,max = 500)
    @Column(nullable = false)//映射为字段 ，值不能为空
    private  String content;

   @OneToOne(cascade = CascadeType.DETACH,fetch=FetchType.LAZY)
   @JoinColumn(name="user_id")
   private User user;



    @Column(nullable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Timestamp createTime;


    protected Comment(){

    }


}
