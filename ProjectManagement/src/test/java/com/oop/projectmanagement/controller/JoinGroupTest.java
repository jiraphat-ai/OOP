// package com.oop.projectmanagement.controller;

// import com.oop.projectmanagement.FirebaseInitializer;
// import org.junit.jupiter.api.*;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.junit.jupiter.api.Assertions.*;
// import java.util.List;
// import com.oop.projectmanagement.model.GroupFordetail;
// import java.util.ArrayList;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
// import javax.servlet.http.HttpSession;

// @WebMvcTest(JoinGroup.class)
// public class JoinGroupTest {
// 	@Autowired
// 	private MockMvc mockMvc;

// 	@MockBean
// 	private FirebaseInitializer firebaseInitializer;

// 	@Autowired
// 	private JoinGroup joinGroup;

// 	@Test
// 	public void cancelRequest() throws Exception {
// 		this.mockMvc.perform(post("/cancelRequest")
// 			.param("documentId", "abc"))
// 			.andExpect(status().isOk())
// 			.andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
// 			.andExpect(jsonPath("$.id").value("abc"));
// 	}

// 	@Test
// 	public void joingroup() throws Exception {
// 		this.mockMvc.perform(get("/loadstatus")
// 				.param("documentId", "abc"))
// 			.andExpect(status().isOk())
// 			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
// 			.andExpect(jsonPath("$.<key>").value("<value>"));
// 	}

// 	@Test
// 	public void joinGroup() throws Exception {
// 		this.mockMvc.perform(post("/joinGroup")
// 				.param("documentId", "abc"))
// 			.andExpect(status().isOk())
// 			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
// 			.andExpect(jsonPath("$.<key>").value("<value>"));
// 	}

// 	@Test
// 	public void searchDocumentById() {
// 		String documentId = "abc";
// 		List<GroupFordetail> expected = new ArrayList<>();
// 		List<GroupFordetail> actual = joinGroup.searchDocumentById(documentId);

// 		assertEquals(expected, actual);
// 	}

// 	@Test
// 	public void sortGroupBySelection() throws Exception {
// 		this.mockMvc.perform(post("/sortGroupBySelection")
// 				.param("sortOption", "abc"))
// 			.andExpect(status().isOk())
// 			.andExpect(view().name(""))
// 			.andExpect(model().attributeExists("<name>"))
// 			.andExpect(model().attribute("<name>", "<value>"))
// 			.andExpect(content().string(""));
// 	}

// 	@Test
// 	public void getlastsearch() {
// 		HttpSession session = null;
// 		List<GroupFordetail> expected = new ArrayList<>();
// 		List<GroupFordetail> actual = joinGroup.getlastsearch(session);

// 		assertEquals(expected, actual);
// 	}


	@Test
	public void getGroupsBySubjectId() {
		String subjectID = "abc";
		String section = "abc";
		List<String> tag = new ArrayList<>();
		List<GroupFordetail> expected = new ArrayList<>();
		List<GroupFordetail> actual = joinGroup.getGroupsBySubjectId(subjectID, section, tag);


// 		assertEquals(expected, actual);
// 	}

// 	@Test
// 	public void searchGroup() throws Exception {
// 		this.mockMvc.perform(get("/searchgroup")
// 				.param("section", "123")
// 				.param("subjectID", "abc"))
// 			.andExpect(status().isOk())
// 			.andExpect(view().name(""))
// 			.andExpect(model().attributeExists("<name>"))
// 			.andExpect(model().attribute("<name>", "<value>"))
// 			.andExpect(content().string(""));
// 	}

// 	@Test
// 	public void getGroupMoreDetail() throws Exception {
// 		this.mockMvc.perform(get("/moreGroupFordetail")
// 				.param("documentId", "abc"))
// 			.andExpect(status().isOk())
// 			.andExpect(view().name(""))
// 			.andExpect(model().attributeExists("<name>"))
// 			.andExpect(model().attribute("<name>", "<value>"))
// 			.andExpect(content().string(""));
// 	}

// 	@Test
// 	public void getUserinfo() throws Exception {
// 		this.mockMvc.perform(get("/joingroup"))
// 			.andExpect(status().isOk())
// 			.andExpect(view().name(""))
// 			.andExpect(model().attributeExists("<name>"))
// 			.andExpect(model().attribute("<name>", "<value>"))
// 			.andExpect(content().string(""));
// 	}
// }
