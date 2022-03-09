package com.forq.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * author long
 *
 * @date 2022/3/8
 * @apiNote
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
@Entity

public class Vote implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Column(nullable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Timestamp createTime;

    protected Vote(){

    }





}
