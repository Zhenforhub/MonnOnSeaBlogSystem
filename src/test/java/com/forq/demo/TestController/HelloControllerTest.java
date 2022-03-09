package com.forq.demo.TestController;

import org.apache.tomcat.jni.Status;
import org.junit.Test;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.awt.*;

/**
 * author long
 *
 * @date 2022/3/4
 * @apiNote
 */

@RunWith(SpringRunner.class)//我想知道runwith 有什么用？
@SpringBootTest
@AutoConfigureMockMvc//意思是自动配置 有关web servelet 的测试模型 mock MVC

/**
 * 意思是配置好该测试模型以后就可以对springmvc进行测试
 * 实际上我也可以认为是对前端的效果进行测试
 */
public class HelloControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Test
    public void testHello() throws Exception {
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get("/hello")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(ResultMatcher.matchAll())
                ;


        /**
         * 首先就说句实话，就是对于mock还是了解太少，对于mock的测试不是很了解
         * 另外代码的测试
         */
        /**
         * 虽然代码不是很了解，而且少了点什么东西
         * 但是并不是说代码不需要写了
         * 也不是代码就不要了
         * 
         */


    }
}
