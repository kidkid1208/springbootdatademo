package com.lzh.Controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mvc;

    @Before
    public void setupMockMvc(){
        mvc = MockMvcBuilders.webAppContextSetup(wac).build(); //初始化MockMvc对象
    }

    @Test
    public void userList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/userList")
                .contentType(new MediaType("application", "json", StandardCharsets.UTF_8))
                .accept(new MediaType("application", "json", StandardCharsets.UTF_8))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].username").value("www"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void addUser() throws Exception {
        //String json={ \"userId\": 3,\"userName\": \"tom\"}"
        mvc.perform(MockMvcRequestBuilders.post("/addUser")
                .contentType(new MediaType("application", "json", StandardCharsets.UTF_8))
                .content("{ \"username\": \"tom\",\"password\": \"tom\"}") //传json参数
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("添加成功"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void updateUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/updateUser")
                .contentType(new MediaType("application", "json", StandardCharsets.UTF_8))
                .content("{ \"id\": 10,\"username\": \"chasel\",\"password\": \"lzh960206\"}") //传json参数
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("更新成功"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void deleteUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/deleteUser/8")
                .accept(new MediaType("application", "json", StandardCharsets.UTF_8))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("删除成功"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void queryUserById() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/queryUserById/4")
                .contentType(new MediaType("application", "json", StandardCharsets.UTF_8))
                .accept(new MediaType("application", "json", StandardCharsets.UTF_8))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("www"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("999"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void getUser() {
    }
}