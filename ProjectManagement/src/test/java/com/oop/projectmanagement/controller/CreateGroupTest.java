package com.oop.projectmanagement.controller;

import com.google.cloud.firestore.*;
import com.oop.projectmanagement.FirebaseInitializer;
import com.oop.projectmanagement.model.Group;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.mock.web.MockHttpSession;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreateGroupTest {

    @InjectMocks
    private CreateGroup createGroup;

    @Mock
    private FirebaseInitializer firebaseInitializer;

    @Mock
    private Firestore firestore;

    @Mock
    private DocumentReference documentReference;

    @Mock
    private CollectionReference collectionReference;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(firebaseInitializer.getDb()).thenReturn(firestore);
        when(firestore.collection(anyString())).thenReturn(collectionReference);
        when(collectionReference.add(any(Group.class))).thenReturn(ApiFutures.immediateFuture(documentReference));
        when(documentReference.get()).thenReturn(ApiFutures.immediateFuture(mock(DocumentSnapshot.class)));
    }

    @Test
    public void testCreateGroup() throws ExecutionException, InterruptedException {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "testUser");
        session.setAttribute("documentId", "testDocumentId");
        Group group = new Group();
        group.setSubjectID("testSubjectId");

        String result = createGroup.createGroup(session, group);

        assertEquals("Group created successfully", result);
    }
}
