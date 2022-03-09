package com.forq.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * author long
 *
 * @date 2022/3/9
 * @apiNote
 */
@Entity
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class Catalog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "名称不能为空")
    @Size(min = 2,max=30)
    @Column(nullable = false)
    private String name;

    @OneToOne(cascade=CascadeType.DETACH,fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    protected Catalog(){

    }
    public Catalog(User user,String name){

    }


}
