package com.forq.demo.controller;

import com.forq.demo.pojo.Catalog;
import com.forq.demo.pojo.User;
import com.forq.demo.service.CatalogService;
import com.forq.demo.util.ConstraintViolationExceptionHandler;
import com.forq.demo.vo.CatalogVo;
import com.forq.demo.vo.Response;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * author long
 *
 * @date 2022/3/10
 * @apiNote
 */
@RestController
@RequestMapping("/catalogs")
public class CatalogController {
    @Autowired
    private CatalogService catalogService;

    @Resource
    /**
     * 如果接口出现两个以上的实现类
     * 再用Autowied就不合适了
     */
    private UserDetailsService userDetailsService;

    /**
     * 获取分类列表
     * @param username
     * @param model
     * @return
     */

    @GetMapping()
    public String listComments(@RequestParam(value = "username",required = true) String username, Model model){
        User user=(User)userDetailsService.loadUserByUsername(username);
        /**
        这个业务接口允许通过用户名来找到用户 这就意味着用户名必须也是唯一的
        至少在用户那必须是唯一的。
         */
        List<Catalog> catalogs=catalogService.ListCatalog(user);

        //判断操作用户是否为分类的所有者

        boolean isOwner=false;
        if(SecurityContextHolder.getContext().getAuthentication()!=null
        &&SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
        &&SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonyousUser")){
            User principal=(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if(principal!=null&&principal.getUsername().equals(user.getUsername())){
                isOwner=true;
            }
        }

        model.addAttribute("isCatalogOwner",isOwner);
        model.addAttribute("catalogs",catalogs);

        return "/userspace/u :: #catalogRepleace";



    }
    /**
     * 创建分类
     * @param  catalogVo
     * @return
     *
     */

    @PostMapping
    @PreAuthorize("authentication.name.equals(#catalogVo.username)")
    //指定的用户才能操作方法
    public ResponseEntity<Response> create (@RequestBody CatalogVo catalogVo){
        String username=catalogVo.getUsername();
        Catalog catalog=catalogVo.getCatalog();

        User user=(User)userDetailsService.loadUserByUsername(username);

        try{
            catalog.setUser(user);
            catalogService.saveCatalog(catalog);
        }catch (ConstraintViolationException e){

            return  ResponseEntity.ok()
                    .body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }catch (Exception e){
            return ResponseEntity.ok()
                    .body(new Response(false,e.getMessage()));
        }

        return ResponseEntity.ok()
                .body(new Response(true,"处理成功",null));
    }


    /**
     * 删除分类
     * @param username
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> delete(String username,Long id){
        try{
            catalogService.removeCatalog(id);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok()
                    .body(new Response(false,ConstraintViolationExceptionHandler.getMessage(e)));
        }catch (Exception e){
            return ResponseEntity.ok()
                    .body(new Response(false,e.getMessage()));
        }

        return  ResponseEntity.ok()
                .body(new Response(true,"删除分类成功!",null));
    }

    /**
     * 获取分类编辑界面
     * @param model
     * @return
     */

    @GetMapping("/edit")
    public String getCatalogEdit(Model model){
        Catalog catalog=new Catalog(null,null);
        model.addAttribute("catalog",catalog);
        return "/userspace/catalogedit";
    }

    @GetMapping("/edit/{id}")
    public String getCatalogById(@PathVariable("id") Long id,Model model){
        Optional<Catalog> optionalCatalog=catalogService.getCatalogById(id);
        Catalog catalog=null;
        if(optionalCatalog.isPresent()){
            catalog=optionalCatalog.get();

        }
        model.addAttribute("catalog", catalog);
        return "/userspace/catalogedit";
    }


}
