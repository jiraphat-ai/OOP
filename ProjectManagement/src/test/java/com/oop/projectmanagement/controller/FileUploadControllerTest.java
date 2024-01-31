package com.oop.projectmanagement.controller;

import com.oop.projectmanagement.FirebaseInitializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileUploadController.class)
public class FileUploadControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FirebaseInitializer firebaseInitializer;

	@Test
	public void uploadCSVFile() throws Exception {
		this.mockMvc.perform(post("/upload")
				.param("file", "<value>"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
	}
}
