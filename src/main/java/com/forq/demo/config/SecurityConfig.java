package com.forq.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;



/**
 * author long
 *
 * @date 2022/3/4
 * @apiNote
 */
@EnableWebSecurity

/**
 * 首先是进行自定义的配置
 *
 */
/**
 * 为了配置方法级别的安全设置 在整个的配置类上加入@EnableGlobalMethodSecurity
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
/**
 * 加入这个注解以后，就开启了方法级别的安全设置了。
 */
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /*8
    必须继承 web安全配置适配器
    这样才能保证web 的安全性
     */
    private static final String KEY="waylau.com";

    /**
     * 紧接着添加相关依赖注入
     *

     */
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
        //意思是使用 BCrypt 加密
    }
    /**
     * 很明显 可以看出来
     * 对于利用容器管理bean
     * 再由容器管理bean接口
     * 这样的话容器就可以通过bean接口去调用bean
     *
     */
    @Bean
    public AuthenticationProvider authenticationProvider(){

        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        //DAO的认证提供者 创建好

        authenticationProvider.setUserDetailsService(userDetailsService);
        //这个认证提供 还需要设置 用户细节 这都是springsecurity 提供的
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        //这个认证提供 还需要设置 密码 设置加密的方式
        return  authenticationProvider;
        /**
         * 最终的目的不过是提供一个认证类帮助认证
         */

    }
    /**
     * 认证信息管理
     * @param auth
     * @throws Exception
     *
     */

    /**
     *
     * @param http
     * @throws Exception
     * 增加具体的权限管理还在后面
     */
    /**
     * 正式对securityconfig 进行配置
     * 目的是：配置角色与资源的关系，规定哪些角色访问哪些资源
     * 启用 c s r f 防护， 并设置部分资源 无需防护
     * 启用记住我的功能
     * @param http
     * @throws Exception
     */
    @Override
    protected  void configure(HttpSecurity http) throws Exception {


        //针对http 请求的安全配置
        /**
         * 保证请求时合法 的 合法的请求 可以合法的申请合法的资源
         * 权限管理相关问题
         * 主要是要依靠springconfig配置
         * 特别是对于请求的拦截
         *
         */
        http.authorizeRequests().antMatchers("/css/**","/js/**","/fonts/**","/index")
                .permitAll().//以上都可以进行访问
                antMatchers("/users/**").hasRole("ADMIN")//需要相应的角色才能进行访问
                .and()
                .formLogin()//基于表单登录验证

                .loginPage("/login")
                .failureUrl("/login-error")//自定义的登录界面
                .and()
                .rememberMe()
                .key(KEY)//意思是启用 remindme也就是启用记住我的功能
                .and()
                .exceptionHandling()
                .accessDeniedPage("/403");//处理异常 ，拒觉访问就会重定向到403页面

        /**
         * 这是针对http请求而言的
         */
        http.csrf().ignoringAntMatchers("/h2-console/**");
        //意思是禁用了 h2控制台的csrf防护

        http.headers().frameOptions().sameOrigin();
        //允许同一来源的h2控制台的请求

        /**
         * 几个要注意的内容
         *
         * 由于管理员要访问h2数据库的控制台，因此 h2-console路径下的资源被设置为permitALL
         * 也就是允许所有人访问。
         * 同时，为了不对h2资源进行 csrf防护，采用了 禁用 csrf 防护功能
         *
         * 而常量key 是设置用于识别记住我的 身份验证而创建的令牌的键
         *
         */




    }
    /**
     * 认证信息管理
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

       /* auth.inMemoryAuthentication()//认证的信息存储与内存中
                .withUser("waylau")
                .password("123456")
                .roles("ADMIN");*/

        /**
         * 如果理解基于内存你的身份认证管理器
         *
         */

        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(authenticationProvider());

        /**
         * 为了启用 方法级别的安全设置
         * 需要在配置类上加上 @EnableGlobalMethodSecurity (prePostEnabled=true)
         * 注解
         *
         */

    }

}
