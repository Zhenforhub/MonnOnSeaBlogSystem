package com.forq.demo.pojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
public class User  extends BasicEntity implements UserDetails , Serializable {

    @NotEmpty(message = "姓名不能为空")
    @Size(max = 50)
    @Column(nullable = false, length = 20)//这行什么意思 表示映射为字段 且不能为空
    private String name;

    @NotEmpty(message = "邮箱不能为空")
    @Size(max = 50)//对字符串的长度进行限制着点也是必要的
    @Email(message="邮箱格式不对")//这是hibernates提供的特性
    @Column(nullable = false, length = 50, unique = true) //邮箱也必须不为空 并且长度被限制在50以内，保证其绝对唯一
    private String email;

    /**
     * 因此我们可以知道，一个字段或者说对象的一个属性为了要保证其表的属性
     * 首先保证其属性值不为空
     * 同时保证其长度在限定范围之内
     * 最后保证其字段值不为空


     */

    @NotEmpty(message="账号不能为空")
    @Size(min=3, max=20)
    @Column(nullable=false,length=20,unique = true)
    private String username;

  @NotEmpty(message="密码不能为空")
  @Size(min=10,max=100)
  @Column(nullable=false,length=100)

  private String password;


  @Column(length=200)//意思就是说头像可以没有，但是如果有一定是一个成熟的字段

  private String avatar;//所谓的avatar就是头像地址



    /**
     * 实现Userdetail接口
     * 并且建立与Authority的联系
     *
     * @return
     */
    /**
     * 建立与Authority的联系方式是通过注解
     * @return
     */
    @ManyToMany(cascade = CascadeType.DETACH,fetch = FetchType.EAGER)
    //阐述两个对象或者说两张表之间是多对多的关系
    @JoinTable(name="user_authority",
            joinColumns = @JoinColumn(name="user_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="authority_id",referencedColumnName = "id")
    )//连接表与表之间的关系，使用表来授权 表 名

    private List<Authority> authorities;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        /**
         * 第一个方法有什么作用？
         * 具体应该做哪些工作
         * 它说需要list<Authority> 转成 List<SimpleGrantedAuthority>,否则前端拿不到角色列表名称
         *
         */
        List<SimpleGrantedAuthority> simpleGrantedAuthorities =new ArrayList<>();
        /*8
        拿到角色列表
         */
        for(GrantedAuthority authority:this.authorities){
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }
        /**
         * 通过foreach循环来将原来权限的数据经过方法 getAuthority 转成 simpleGrantedAuthoritie
         * 最后被列表接收
         * 最后返回权限列表 或者返回角色列表
         */
        return simpleGrantedAuthorities;
    }

    /**
     * 针对实体类做修改
     * 主要是增加加密密码的方法
     * @return
     */
    /**
     * 实现密码的加密
     * @return
     */
    public void setEnCodePassWord(String password){
        PasswordEncoder encoder=new BCryptPasswordEncoder();
        String encodedPassword=encoder.encode(password);
        /**
         * 不对我刚才搞错了
         * encode是加密
         * decode才是解密
         */
        this.password=encodedPassword;
    }

    @Override
    public String getPassword() {
        return null;
    }


    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    /**
     * 首先要说明一点
     * @NotEmpty @Email都是
     */

}
