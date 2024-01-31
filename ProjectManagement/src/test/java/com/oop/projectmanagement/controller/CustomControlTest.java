package com.oop.projectmanagement.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.oop.projectmanagement.controller.CustomControl;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import com.oop.projectmanagement.model.User;
import com.oop.projectmanagement.model.GroupFordetail;
import com.oop.projectmanagement.model.Member;
import com.oop.projectmanagement.controller.CustomControl;

public class CustomControlTest {
	@Test
	public void getDocumentFeildByDocRef() throws InterruptedException, ExecutionException {
		CustomControl c = new CustomControl();
		DocumentReference docRef = Mockito.mock(DocumentReference.class);
		ApiFuture<DocumentSnapshot> future = Mockito.mock(ApiFuture.class);
		DocumentSnapshot document = Mockito.mock(DocumentSnapshot.class);
	
		String field = "";
		String expected = "";
	
		Mockito.when(docRef.get()).thenReturn(future);
		Mockito.when(future.get()).thenReturn(document);
		Mockito.when(document.getString(field)).thenReturn(expected);
	
		String actual = c.getDocumentFeildByDocRef(docRef, field);
	
		assertEquals(expected, actual);
	}

	@Test
	public void getDocumentIdByDocRefTest() throws InterruptedException, ExecutionException {
		// Arrange
		CustomControl c = new CustomControl();
		Firestore db = Mockito.mock(Firestore.class);
		DocumentReference docRefMock = Mockito.mock(DocumentReference.class);
		ApiFuture<DocumentSnapshot> future = Mockito.mock(ApiFuture.class);
		DocumentSnapshot document = Mockito.mock(DocumentSnapshot.class);
	
		String docRef = "collection/document";
		String expectedId = "";
	
		Mockito.when(db.document(docRef)).thenReturn(docRefMock);
		Mockito.when(docRefMock.get()).thenReturn(future);
		Mockito.when(future.get()).thenReturn(document);
		Mockito.when(document.exists()).thenReturn(false);
	
		// Act
		String actualId = c.getDocumentIdByDocRef(docRef);
	
		// Assert
		assertEquals(expectedId, actualId);
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
