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

@WebMvcTest(JoinGroup.class)
public class JoinGroupTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FirebaseInitializer firebaseInitializer;

	@Test
	public void cancelRequest() throws Exception {
		this.mockMvc.perform(post("/cancelRequest")
			.param("documentId", "abc"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
			.andExpect(jsonPath("$.id").value("abc"));
	}
}
