package com.techacademy.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.techacademy.entity.User;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class UserControllerTest {
    private MockMvc mockMvc;

    private final WebApplicationContext webApplicationContext;

    UserControllerTest(WebApplicationContext context) {
        this.webApplicationContext = context;
    }

    @BeforeEach
    void beforeEach() {
        // Spring Securityを有効にする
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity()).build();
    }

    @Test
    @DisplayName("User一覧画面")
    @WithMockUser
    void testGetList() throws Exception {
        // HTTPリクエストに対するレスポンスの検証
        MvcResult result = mockMvc.perform(get("/user/list")) // URLにアクセス
                .andExpect(status().isOk()) // HTTPステータスが200OKであること
                .andExpect(model().attributeExists("userlist")) // Modelにuserlistが含まれていること
                .andExpect(model().hasNoErrors()) // Modelにエラーが無いこと
                .andExpect(view().name("user/list")) // viewの名前が user/list であること
                .andReturn(); // レスポンス内容を取得

        // Modelからuserlistを取り出す
        Object userlistObject = result.getModelAndView().getModel().get("userlist");
        if (userlistObject instanceof List<?>) {
            @SuppressWarnings("unchecked")
            List<User> users = (List<User>) userlistObject;
            assertEquals(3, users.size()); // 件数が3件であることを検証

            // userlistから1件ずつ取り出し、idとnameを検証する
            User firstUser = users.get(0);
            assertEquals(1, firstUser.getId()); // idの検証
            assertEquals("キラメキ太郎", firstUser.getName()); // nameの検証

            User secondUser = users.get(1);
            assertEquals(2, secondUser.getId()); // idの検証
            assertEquals("キラメキ次郎", secondUser.getName()); // nameの検証

            User thirdUser = users.get(2);
            assertEquals(3, thirdUser.getId()); // idの検証
            assertEquals("キラメキ花子", thirdUser.getName()); // nameの検証
        }

    }
}