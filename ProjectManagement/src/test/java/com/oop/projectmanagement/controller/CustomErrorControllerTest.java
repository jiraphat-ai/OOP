package com.oop.projectmanagement.controller;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CustomErrorControllerTest {

    @Test
    void handleError_ShouldReturnErrorView() throws Exception {
        // Arrange
        CustomErrorController errorController = new CustomErrorController();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(errorController).build();

        // Act & Assert
        mockMvc.perform(get("/error"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    void getErrorPath_ShouldReturnErrorPath() {
        // Arrange
        CustomErrorController errorController = new CustomErrorController();

        // Act & Assert
        String errorPath = errorController.getErrorPath();
        assert errorPath.equals("/error");
    }
}
