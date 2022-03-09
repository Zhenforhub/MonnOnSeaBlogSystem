package com.forq.demo.vo;

/**
 * author long
 *
 * @date 2022/3/5
 * @apiNote
 */

import org.springframework.http.ResponseEntity;

/**
 * 保证 返回结果的统一性
 * 可以说是保证结果的统一性
 * 所以从控制器中的结果还要进一步进行封装才能返回
 */
public class Response {

    private boolean success;//判断返回是否成功

    private String message;//消息

    private Object body;//返回的响应体

    /**
     * 响应处理是否成功
     * @return
     */
    public boolean isSuccess(){
        return success;
    }

    public void setSuccess(boolean success){
        this.success=success;
    }
    /**
     * 响应处理消息
     */
    public String getMessage(){

        return message;
    }

    public void setMessage(String message){
        this.message=message;
    }
    /**
     * 响应处理的返回内容
     */
    public Object getBody(){

        return body;
    }
    public void setBody(Object body){
        this.body=body;
    }
    /**
     * 另外就是多种构造方法
     * 保证响应的准确
     */
    public Response(boolean success,String message){
        this.success=success;
        this.message=message;
        /**
         * 这正是为了适应有些是没有返回结果的所构造的构造函数
         */

    }
    public Response (boolean success,String message,Object body){
        this.success=success;
        this.message=message;
        this.body=body;
    }

}
