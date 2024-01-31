package com.oop.projectmanagement.controller;

import com.oop.projectmanagement.FirebaseInitializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.hamcrest.Matchers.containsString;

@WebMvcTest(TemplateStudentController.class)
public class TemplateStudentControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FirebaseInitializer firebaseInitializer;

	@Test
	public void getBar() throws Exception {
		this.mockMvc.perform(get("/template_student"))
			.andExpect(status().isOk())
			.andExpect(view().name("template_student"))            
			.andExpect(content().string(containsString("<!DOCTYPE html>")));  
	}
}
