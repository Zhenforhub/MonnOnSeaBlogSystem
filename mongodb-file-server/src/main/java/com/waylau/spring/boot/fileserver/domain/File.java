package com.waylau.spring.boot.fileserver.domain;

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
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Objects.*;

/**对于文档来说
 * 建立其实体 就需要用到 注解 @document

 */
@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
public class File implements Serializable {
    @Id//表明其主键身份
    private String id;//主键

    private String name;//文件名称
    private String contentType;//文件类型
    private Long size;
    private Date uploadDate;
    private String md5;
    private Binary content;//文件内容
    private String path;//文件路径




    /**
     * 重写这个方法的目的是什么？
     *
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object){
        if(this==object){
            return true;
            /**
             * 如果相等说明是同类型
             */
        }
        if(object==null||getClass()!=object.getClass()){
            return false;
            /*8
            那么如果不相等 要判断
            如果为空 或者 字节码文件不相同
            直接返回false
             */
        }
        /*8
        那么如果不是以上这两种情况
        就说明object 类 有可能是 名为文件的文件类的父类
        实际在运行时字节码 为文件类


         */
        File fileinfo=(File)object;
        /**
         * 对object 进行强制类型转换 并把引用赋给 fileinfo
         */

        return Objects.equals(size,fileinfo.size)
                &&Objects.equals(name,fileinfo.name)
                &&Objects.equals(contentType,fileinfo.contentType)
                &&Objects.equals(uploadDate,fileinfo.uploadDate)
                &&Objects.equals(md5,fileinfo.md5)
                &&Objects.equals(id, fileinfo.id);

    }
    @Override
    public int hashCode(){
        return Objects.hash(name,contentType,size,uploadDate,md5,id);
        /**
         * 这种哈希办法我还是第一次见
         * 利用工具类中object hash方法 针对字段做哈希
         */
    }
    @Override
    public String toString(){
        return "File{"
                +"name='"+name+"\'"
                +", contentType='"+contentType+"\'"
                +", size="+size
                +", uploadDate=" +uploadDate
                +", md5='" +md5+"\'"
                +", id"+id+"\'"
                +"}";

    }

}
