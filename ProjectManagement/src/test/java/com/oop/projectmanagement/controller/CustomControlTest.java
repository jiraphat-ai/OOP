package com.oop.projectmanagement.controller;

import com.google.cloud.firestore.DocumentReference;
import com.oop.projectmanagement.controller.CustomControl;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import com.oop.projectmanagement.model.User;
import com.oop.projectmanagement.model.GroupFordetail;
import com.oop.projectmanagement.model.Member;

public class CustomControlTest {
@Test
public void getDocumentFeildByDocRef() throws InterruptedException, ExecutionException {
    CustomControl c = new CustomControl();
    DocumentReference docRef = Mockito.mock(DocumentReference.class);
    String field = "abc";
    String expected = "abc";
    String actual = c.getDocumentFeildByDocRef(docRef, field);

    assertEquals(expected, actual);
}

	@Test
	public void getDocumentIdByDocRef() throws InterruptedException, ExecutionException {
		CustomControl c = new CustomControl();
		String docRef = "abc";
		String expected = "abc";
		String actual = c.getDocumentIdByDocRef(docRef);

		assertEquals(expected, actual);
	}

	@Test
	public void getDocumentRefSubjectFromSubjectID() {
		CustomControl c = new CustomControl();
		String subjectId = "abc";
		String expected = "abc";
		String actual = c.getDocumentRefSubjectFromSubjectID(subjectId);
	
		assertEquals(expected, actual);
	}

	@Test
public void getAllUsersFromMembers() {
    CustomControl c = new CustomControl();
    String docId = "abc";
    ArrayList<User> expected = new ArrayList<>();
    ArrayList<Member> actual = c.getAllUsersFromMembers(docId);

    assertEquals(expected, actual);
}

	@Test
	public void getGroupDetail() {
		CustomControl c = new CustomControl();
		String documentId = "abc";
		GroupFordetail expected = new GroupFordetail();
		GroupFordetail actual = c.getGroupDetail(documentId);

		assertEquals(expected, actual);
	}
}
