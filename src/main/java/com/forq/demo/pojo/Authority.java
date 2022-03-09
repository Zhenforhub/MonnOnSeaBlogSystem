package com.forq.demo.pojo;

/**
 * author long
 *
 * @date 2022/3/6
 * @apiNote
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * 创建  实体类
 * Authority 表示权限 等同于角色
 *
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Authority implements GrantedAuthority {
    /**
     * 实现security的相关权限实体接口

     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;//表示用户的唯一标识

    @Column(nullable = false)//映射字段不能为空
    private String name;


    @Override
    public String getAuthority() {
        return name;
    }
}
/**
 * 既然要涉及权限的管理
 * 必要与用户进行交互
 *
 */
