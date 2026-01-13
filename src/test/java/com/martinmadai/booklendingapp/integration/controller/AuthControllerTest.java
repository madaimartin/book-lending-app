package com.martinmadai.booklendingapp.integration.controller;

import com.martinmadai.booklendingapp.domain.user.controller.AuthController;

import com.martinmadai.booklendingapp.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    /*
     * Broken Controller test will be fixed soon...
     */

     /*
    @Test
    void getLoginPage_ok() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    @Test
    void register_invalidForm_returnsRegisterPage() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "")
                        .param("password", "")
                        .param("email", "invalid")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"));

        verifyNoInteractions(userService);
    }


    @Test
    void register_validForm_redirectsToLogin() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "john")
                        .param("password", "secret")
                        .param("email", "john@test.com")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered"));

        verify(userService).registerUser(any(UserRegisterDto.class));
    }
     */
}