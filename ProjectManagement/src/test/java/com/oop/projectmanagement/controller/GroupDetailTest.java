package com.oop.projectmanagement.controller;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.oop.projectmanagement.FirebaseInitializer;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(GroupDetail.class)
public class GroupDetailTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FirebaseInitializer firebaseInitializer;

    @Mock
    private Firestore db;

    @Mock
    private CollectionReference collectionReference;


    // @BeforeEach
    // public void setUp() {
    //     // Assuming that FirebaseInitializer has a method called someMethod that returns a String
    //     when(firebaseInitializer.someMethod()).thenReturn("mock value");
    // }

	@Test
	public void getUserinfo() throws Exception {
		this.mockMvc.perform(get("/groupdetail")
				.param("documentId", "abc"))
			.andExpect(status().isOk())
			.andExpect(view().name(""))
			.andExpect(model().attributeExists("<name>"))
			.andExpect(model().attribute("<name>", "<value>"))
			.andExpect(content().string(""));
	}

	@Test
	public void deleteGroup() throws Exception {
		this.mockMvc.perform(post("/deleteGroup")
				.param("documentId", "abc"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$.<key>").value("<value>"));
	}

	@Test
	public void editGroup() throws Exception {
		this.mockMvc.perform(post("/editGroup")
				.param("documentId", "abc")
				.param("groupDescription", "abc")
				.param("groupName", "abc"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$.<key>").value("<value>"));
	}

	@Test
	public void setRole() throws Exception {
		this.mockMvc.perform(post("/setRole")
				.param("documentId", "abc")
				.param("groupId", "abc")
				.param("role", "abc"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$.<key>").value("<value>"));
	}

	@Test
	public void deleteMember() throws Exception {
		this.mockMvc.perform(post("/deleteMember")
				.param("documentId", "abc")
				.param("groupId", "abc"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$.<key>").value("<value>"));
	}
}
