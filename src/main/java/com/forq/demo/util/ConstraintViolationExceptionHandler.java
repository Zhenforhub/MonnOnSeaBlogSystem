package com.forq.demo.util;

/**
 * author long
 *
 * @date 2022/3/5
 * @apiNote
 */

import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * 为了保证用户管理模块的安全性和健壮性
 * 要增加相关异常的处理类
 * 来负责处理异常
 */
public class ConstraintViolationExceptionHandler {
    /**
     * 获取批量异常信息
     * @param  e
     * @return
     */
    public static String getMessage(ConstraintViolationException e){

        List<String > msgList=new ArrayList<>();

        for(ConstraintViolation<?> constraintViolation: e.getConstraintViolations()){

            msgList.add(constraintViolation.getMessage());
        }
        /**
         * 首先这是一个foreach循环 并且 循环的目标是 e也就是异常 的元素 也即是 constarinViolation
         * 但是不确定是有多少个 所以采用数组列表来接收
         */
        String message = StringUtils.join(msgList.toArray(), ";");

        return message;

        /**
         * 这两段的意思就是 我利用StringUtils工具类 将列表数组进行分割并变成统一的字符串最后返回
         * 这就是处理异常信息的方式
         * 但是我还不是很懂这种处理异常信息的方式
         */




    }
}
