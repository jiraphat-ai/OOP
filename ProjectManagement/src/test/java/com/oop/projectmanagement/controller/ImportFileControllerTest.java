package com.oop.projectmanagement.controller;

import com.oop.projectmanagement.FirebaseInitializer;
import com.oop.projectmanagement.SpringBootTest;
import com.oop.projectmanagement.model.Subject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.doNothing;
import org.springframework.test.context.junit.jupiter.SpringExtension; // JUnit 5 extension

@WebMvcTest(ImportFileController.class) // Specify the controller class you're testing
public class ImportFileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FirebaseInitializer firebaseInitializer;

    @MockBean
    private Subject subjectServiceMock;

    @Test
    public void deleteSubject() throws Exception {
        // Arrange
        String subjectId = "1";

        // Act & Assert
        mockMvc.perform(delete("/importFile/deleteSubject")
                .param("subjectId", subjectId))
                .andExpect(status().isNotFound());
    }
}