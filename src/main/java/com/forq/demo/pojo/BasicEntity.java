package com.forq.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * author long
 *
 * @date 2022/3/4
 * @apiNote
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
public class BasicEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private    Long   id;
    //实体的唯一标识

    private   Date  created;

    private  Date updated;

}
/**
 * 有关全文搜索的概述
 * 以及在项目中有全文搜索的好处是什么？
 * 这个我还不太清楚
 *
 */
