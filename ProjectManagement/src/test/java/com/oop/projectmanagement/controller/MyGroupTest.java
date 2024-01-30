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

@WebMvcTest(MyGroup.class)
public class MyGroupTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FirebaseInitializer firebaseInitializer;

	@Autowired
	private MyGroup myGroup;

	@Test
	public void getUserGroupsByUsername() throws InterruptedException, ExecutionException {
		String username = "abc";
		ArrayList<GroupFordetail> expected = null;
		ArrayList<GroupFordetail> actual = myGroup.getUserGroupsByUsername(username);

		assertEquals(expected, actual);
	}
}
