package com.oop.projectmanagement.controller;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.concurrent.ExecutionException;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import com.oop.projectmanagement.model.Notification;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(MenubarController.class)
public class MenubarControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FirebaseInitializer firebaseInitializer;

	@Autowired
	private MenubarController menubarController;

	@Test
	public void acceptTask() throws Exception {
		this.mockMvc.perform(post("/accepttask").
		   param("group_id", "abc").
		   param("task_id", "abc")).
		  andExpect(status().isOk()).
		  andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")).
		  andExpect(jsonPath("$.<key>").value("<value>"));
	}

	@Test
	public void rejectTask() throws Exception {
		this.mockMvc.perform(post("/rejecttask").
		   param("group_id", "abc").
		   param("task_id", "abc")).
		  andExpect(status().isOk()).
		  andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)).
		  andExpect(jsonPath("$.<key>").value("<value>"));
	}

	@Test
	public void updateTaskStatus() {
		String groupId = "abc";
		String taskId = "abc";
		String status = "abc";
		menubarController.updateTaskStatus(groupId, taskId, status);
	}

	@Test
	public void getTaskNotifications() throws InterruptedException, ExecutionException {
		HttpSession session = null;
		ArrayList<Notification> expected = null;
		ArrayList<Notification> actual = menubarController.getTaskNotifications(session);

		assertEquals(expected, actual);
	}

	@Test
	public void acceptRequest() throws Exception {
		this.mockMvc.perform(post("/acceptrequest").
		   param("group_id", "abc").
		   param("request_id", "abc")).
		  andExpect(status().isOk()).
		  andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")).
		  andExpect(content().string("expectedString"));
	}

	@Test
	public void rejectRequest() throws Exception {
		this.mockMvc.perform(post("/rejectrequest").
		   param("group_id", "abc").
		   param("request_id", "abc")).
		  andExpect(status().isOk()).
		  andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)).
		  andExpect(jsonPath("$.<key>").value("<value>"));
	}

	@Test
	public void getRequest() throws InterruptedException, ExecutionException {
		HttpSession session = null;
		ArrayList<Notification> expected = null;
		ArrayList<Notification> actual = menubarController.getRequest(session);

		assertEquals(expected, actual);
	}

	@Test
	public void getBar() throws Exception {
		this.mockMvc.perform(get("/menu_bar_student"))
			.andExpect(status().isOk())
			.andExpect(view().name(""))
			.andExpect(model().attributeExists("<name>"))
			.andExpect(model().attribute("<name>", "<value>"))
			.andExpect(content().string(""));
	}
}
