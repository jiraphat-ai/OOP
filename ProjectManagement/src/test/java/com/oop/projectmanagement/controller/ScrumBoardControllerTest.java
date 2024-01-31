package com.oop.projectmanagement.controller;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.oop.projectmanagement.FirebaseInitializer;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.List;
import java.util.concurrent.ExecutionException;

@WebMvcTest(ScrumBoardController.class)
public class ScrumBoardControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FirebaseInitializer firebaseInitializer;

	@Autowired
	private ScrumBoardController scrumBoardController;

@Test
public void deleteTaskTest() throws Exception {
    // Arrange
    Firestore db = Mockito.mock(Firestore.class);
    CollectionReference collectionRefMock = Mockito.mock(CollectionReference.class);

    Mockito.when(db.collection(anyString())).thenReturn(collectionRefMock);

    // Act and Assert
    this.mockMvc.perform(post("/deleteTask"))
        .andExpect(status().isBadRequest());
}

	@Test
	public void changeTaskStatus() throws Exception {
		Firestore db = Mockito.mock(Firestore.class);
		CollectionReference collectionRefMock = Mockito.mock(CollectionReference.class);
		this.mockMvc.perform(post("changeState")
				.param("documentId", "abc")
				.param("status", "abc")
				.param("taskdoc_Id", "abc"))
			.andExpect(status().isOk())
			.andExpect(content().string(""));
	}

	@Test
	public void getTasks() throws Exception {
		Firestore db = Mockito.mock(Firestore.class);
		CollectionReference collectionRefMock = Mockito.mock(CollectionReference.class);
		this.mockMvc.perform(get("/getTasks")
				.param("doc_Id", "abc")
				.param("status", "abc"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$[0].db").value("<value>"))
			.andExpect(jsonPath("$[0].taskName").value("<value>"))
			.andExpect(jsonPath("$[0].member[0]").value("<value>"))
			.andExpect(jsonPath("$[0].deadline").value("<value>"))
			.andExpect(jsonPath("$[0].description").value("<value>"))
			.andExpect(jsonPath("$[0].status").value("<value>"))
			.andExpect(jsonPath("$[0].documentId").value("<value>"));
	}

	@Test
	public String getUserDocumentId(String username) {
		List<String> documentIds = getDocumentIds(username);
		if (documentIds.isEmpty()) {
			throw new IllegalArgumentException("No document ID found for username: " + username);
		}
		return documentIds.get(0);
	}

	private List<String> getDocumentIds(String username) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getDocumentIds'");
	}

	@Test
	public void createTask() throws Exception {
		this.mockMvc.perform(post("/createTask").content("abc").contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isBadRequest());
			//.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			//.andExpect(jsonPath("$.<key>").value("<value>"));
	}
}
