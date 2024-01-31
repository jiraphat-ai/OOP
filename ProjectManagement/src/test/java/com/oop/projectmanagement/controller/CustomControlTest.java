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
		String expected = null;
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
    public void testGetAllUsersFromMembers() throws ExecutionException, InterruptedException {
        String docId = "testDocId";
        when(documentReference.collection(anyString()).get()).thenReturn(ApiFutures.immediateFuture(querySnapshot));
        List<QueryDocumentSnapshot> documents = new ArrayList<>();
        documents.add(queryDocumentSnapshot);
        when(querySnapshot.getDocuments()).thenReturn(documents);
        when(queryDocumentSnapshot.get(anyString())).thenReturn(documentReference);
        when(documentSnapshot.toObject(User.class)).thenReturn(new User());

        ArrayList<User> result = customControl.getAllUsersFromMembers(docId);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
