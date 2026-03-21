package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class FilmControllerTest {
    private static final String PATH = "/films";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilmController filmController;

    @DisplayName("Проверка создания рабочего фильма")
    @Test
    void film_create() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFromFile("film/addFilm/request/filmCreate.json")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(getContentFromFile("film/addFilm/response/filmCreate.json")));
    }

    @DisplayName("Проверка валидации пустого описания")
    @Test
    void validation_empty_description_should_fail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFromFile("film/addFilm/request/filmCreateFailEmptyDescription.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("Проверка валидации описания длинной > 200 символов")
    @Test
    void validation_long_description_should_fail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getContentFromFile("film/addFilm/request/filmCreateFailLongDescription.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("Проверка валидации некорректного названия фильма")
    @Test
    void validation_fail_name_should_fail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContentFromFile("film/addFilm/request/filmCreateFailName.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("Проверка валидации некорректной даты фильма")
    @Test
    void validation_fail_release_date_should_fail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContentFromFile("film/addFilm/request/filmCreateFailReleaseDate.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
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
