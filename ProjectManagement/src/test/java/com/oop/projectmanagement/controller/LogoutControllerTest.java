package com.oop.projectmanagement.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(LogoutController.class)
public class LogoutControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void logout() throws Exception {
		this.mockMvc.perform(get("/logout"))
			.andExpect(status().isFound())
			.andExpect(view().name("redirect:/login"))			.andExpect(content().string(""));
	}
}
