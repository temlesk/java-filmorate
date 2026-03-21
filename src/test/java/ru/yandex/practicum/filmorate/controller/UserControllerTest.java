package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTest {
    private static final String PATH = "/users";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @DisplayName("Проверка валидации создания корректного user")
    @Test
    void user_create() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContentFromFile("user/add/request/userCreate.json")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("Проверка валидации некорректной даты рождения")
    @Test
    void user_create_fail_birthday() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContentFromFile("user/add/request/userCreateFailBirthday.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("Проверка валидации некорректного email")
    @Test
    void user_create_fail_email() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContentFromFile("user/add/request/userCreateFailEmail.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("Проверка валидации некорректного логина")
    @Test
    void user_create_fail_login() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContentFromFile("user/add/request/userCreateFailLogin.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Проверка, что контроллер вообще доступен")
    void simple_test() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andDo(result -> System.out.println("Статус ответа: " + result.getResponse().getStatus()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private static String getContentFromFile(String filename) {
        try {
            return Files.readString(ResourceUtils.getFile("classpath:" + filename).toPath(),
                    StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new RuntimeException("Не открывается файл", exception);
        }
    }
}
