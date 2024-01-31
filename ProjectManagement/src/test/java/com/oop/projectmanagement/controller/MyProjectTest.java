package com.oop.projectmanagement.controller;

import com.oop.projectmanagement.FirebaseInitializer;
import com.oop.projectmanagement.model.GroupFordetail;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(MyProject.class)
public class MyProjectTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FirebaseInitializer firebaseInitializer;

	@Autowired
	private MyProject myProject;

	@Test
	public void getUserGroupsByUsername() throws InterruptedException, ExecutionException {
		String documentId = "abc";
		ArrayList<GroupFordetail> expected = null;
		ArrayList<GroupFordetail> actual = myProject.getUserGroupsByUsername(documentId);

		assertEquals(expected, actual);
	}

	@Test
	public void getUserinfo() throws Exception {
		this.mockMvc.perform(get("/myproject"))
			.andExpect(status().isOk())
			.andExpect(view().name(""))
			.andExpect(model().attributeExists("<name>"))
			.andExpect(model().attribute("<name>", "<value>"))
			.andExpect(content().string(""));
	}
}
