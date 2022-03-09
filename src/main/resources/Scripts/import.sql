/**角色信息是基础信息
不会发生变更
也就是说 不会发生角色变更的数据 可以通过初始化的方式加载
并且 可以采用sql脚本来导入数据库


====


 */
 /**
 只要放在resources 资源文件下
 sql脚本就会在 项目启动时自动运行 完成数据的导入
  */

 insert into user(id, username, password , name, email)
 values(1, 'admin','123456','老为','i@waylau.com');

 insert into user(id,username,password, name, email)
 values(2,'waylau','123456','老卫',"waylau@waulau.com");

 insert into authority(id,name)
 values(1,'ROLE_ADMIN');

 insert into authority(id,name)
values(2,"ROLE_USER");

insert into user_authority( user_id,authority_id)
values(1,2);

insert into user_authority(user_id, authority_id)
values(2,2);
/**
接下来完成对权限管理的实现
 */

