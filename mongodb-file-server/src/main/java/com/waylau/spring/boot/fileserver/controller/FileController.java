package com.waylau.spring.boot.fileserver.controller;

import com.waylau.spring.boot.fileserver.domain.File;
import com.waylau.spring.boot.fileserver.service.FileService;
import com.waylau.spring.boot.fileserver.utils.MD5Util;

import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * author long
 *
 * @date 2022/3/6
 * @apiNote
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
/**
 * 首先对CrossOrigin进行解释
 * 它的属性 oringins=* 表示允许所有域名访问
 * oringins也可以指定只有特定域名才能进行访问
 * 并且maxAge就是准备相应前 缓存持续最大的时间 这里是以秒为单位的
 * 也就是说响应之前的缓存时长可以指定为一个小时。
 */
public class FileController {
    @Autowired
    FileService fileService;
    /**
     * 文件对外提供API
     * 在控制器中有诸多对外提供的方法
     *
     */
    /**
     * 首先要说明的是
     * 控制器 作为实际上的 API提供者
     * 接收用户的请求 和响应
     */

    @Value("${server.address}")
    /**
     * 通过美元符号加大括号
     * 来指定值
     */
    private String serverAddress;

    @Value("${server.port}")
    private String serverPort;

    /**
     * 首先定义两个私有变量
     * 分别是服务地址和 服务端口号
     * 作为小型文件服务器对外开发API的地址和端口号
     * 并且用变量来表示，这样修改起来也比较方法，直接在配置文件中修改变量，就可以改变服务器的地址和端口号
     */

    @RequestMapping(value = "/")
    /**
     * 展示最新的20条数据
     * 它是利用传入model 并且数据 是经过降序 分页查询得到的结果
     * 保证所得到的的数据是最新的20条文件数据
     */
    public String index(Model model) {
        //展示最新的20条数据
        /**
         * 因为一开始按时间进行降序处理
         * 所以能够一直拿到最新的20条数据
         * 牛逼
         */
        model.addAttribute("files", fileService.listFileByPage(0, 20));

        return "index";

    }

    /**
     * 分页查询文件
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("files/{pageIndex}/{pageSize}")
    @ResponseBody
    public List<File> listFilesByPage(@PathVariable int pageIndex, @PathVariable int pageSize) {
        return fileService.listFileByPage(pageIndex, pageSize);
        /**
         *  采用get请求 没有请求体 而且采用变量表达式
         *  返回的结果有响应体
         *  不过我觉得没有响应体也一样
         *  在我这里属于重复注解了
         *
         */
    }

    /**
     * 获取文本信息
     *
     * @param id
     * @return
     */
    @GetMapping("files/{id}")
    @ResponseBody
    public ResponseEntity<Object> serveFile(@PathVariable String id) {

        Optional<File> file = fileService.getFileById(id);
        if (file.isPresent()) {
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + file.get().getName() + "\"")
                    /**
                     * 如果理解这个四个请求头信息
                     * httpheaders 头部信息 包括 disposition httpHeaders 文件类型  头的值为 流
                     * 请求头部信息 让你内容长度  首先他们的值都是字符串类型 字符串类型向外界展示 很闲阿然是获取文件的大小
                     * 最后是连接  连接的值为 close 解释文件已经关闭
                     */
                    .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                    .header(HttpHeaders.CONTENT_LENGTH, file.get().getSize() + "")
                    .header("FileConnection", "close")
                    .body(file.get().getContent().getData());
            /**
             * 这里的意思是说
             * 如果文件存在
             * 就返回文件以响应实体来包装文件
             * 响应实体 包括 oK 状态
             * 并且加入四个请求头信息
             * 再加上请求体 请求体 就是获取了文件的 二进制码的数据 这个方法 getData会返回字节信息
             * 用字节数组来作为响应体来返回
             */

        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("File was not found");
            /**
             * 如果文件为空，那么响应体就只有一个状态
             * 那就是http状态为找不到
             * 请求体也只有一个字符串
             * 表示文件找不到
             */
        }
    }

    /**
     * 在线显示文件
     *
     * @param id
     * @return
     */
    @GetMapping("/view/{id}")
    @ResponseBody
    public ResponseEntity<Object> serveFileOnline(@PathVariable String id) {

        Optional<File> file = fileService.getFileById(id);
        if (file.isPresent()) {
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "fileName=\"" + file.get().getName() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, file.get().getContentType())
                    .header(HttpHeaders.CONTENT_LENGTH, file.get().getSize() + "")
                    .header("Connection", "close")
                    .body(file.get().getContent().getData());

        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("File was not found");

        }
    }

    /**
     * 文件上传
     *
     * @param file
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/")

    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        /**
         * 用来处理文件的上传
         * 包括MultipartFile 用来接收文件的类
         * 还有重定向的属性
         */
        try {
            File f = new File();
            f.setName(file.getOriginalFilename())
                    .setContentType(file.getContentType())
                    .setContent(new Binary(file.getBytes()))
                    .setSize(file.getSize())
                    .setUploadDate(new Date());

            f.setMd5(MD5Util.getMD5(file.getInputStream()));

            fileService.saveFile(f);


            /**
             * 当一个文件对象新创建出来以后
             * 给它设置好 文件名
             * 文件内容类型
             * 还有文件的二进制内容
             * 设置好文件的大小
             * 对文件上传还必须要有你文件上传的时间
             * 这样才是一个完整的文件
             * 所以文件对象被创建出来以后， 就写上上传时间这就合理了
             * 因为要展示 被上传的 20条数据
             * 文件为什么要设置加密 保证文件的安全性
             * 最后文件上传以后入库，交由mongodb数据库来保存
             */
            /**
             * MD5Util到底是什么意思
             * MD5util工具类
             * 能实现getMD5方法
             * 返回的应该是一个经过加密以后的字符串
             */

        } catch (IOException | NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "your" + file.getOriginalFilename() + "is wrong!");
            return "redirect:/";

            /**
             * 针对文件 和流的操作 最容易碰到的运行时异常就是 输入输出流异常
             * 又由于对加密算法
             * 所有又有 没有这样的算法的异常
             * 如果抛出了异常要及时的去打印栈信息
             * 并且加入重定向属性
             * 特别是加入刷新属性 指明文件有问题
             * 然后重定向
             */

        }
        redirectAttributes
                .addFlashAttribute("message", "you successfully upload" + file.getOriginalFilename() + "!");

        return "redirect :/";

        /**
         * 如果文件上传成功 就也需要对重定向属性 增加
         * 属性名就是消
         * 属性值就是  已经成功上传了文件
         */
    }

    /**
     * 上传接口
     *
     * @param file
     * @return
     */

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        File returnFile = null;
        /**
         * 很重要
         * 如果要在try的时候给文件赋值
         * 首先要有一个文件的空的引用
         * 这样的如果捕获到了异常
         * 依然可以通过finally 来返回空文件引用
         */
        try {
            File f = new File();
            f.setName(
                    file.getOriginalFilename())//设置文件名
                    .setContentType(file.getContentType())//设置文件类型
                    .setSize(file.getSize())//设置文件的大小
                    .setContent(new Binary(file.getBytes()));//设置文件二进制数值内容 也就是将字节内容转换成二进制
            /**
             * 因为没有按照传统的get setter 方法进行注入
             * 所以只好采取这种赋值办法
             */

            f.setMd5(MD5Util.getMD5(file.getInputStream()));
            returnFile = fileService.saveFile(f);

            String path = "//" + serverAddress + ":" + serverPort + "/view/" + returnFile.getId();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(path);

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
        /**
         * 很显然这个是上传对外暴露的接口
         * 上传对外暴露的接口
         */

    }

    /**
     * 删除文件
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteFileById(@PathVariable String id) {
        try {
            fileService.removeFile(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("DELETE Success!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}

