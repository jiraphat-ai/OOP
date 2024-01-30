package com.oop.projectmanagement.controller;

import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.*;
import com.oop.projectmanagement.FirebaseInitializer;
import com.oop.projectmanagement.model.GroupFordetail;
import com.oop.projectmanagement.model.User;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomControlTest {

    @InjectMocks
    private CustomControl customControl;

    @Mock
    private FirebaseInitializer firebaseInitializer;

    @Mock
    private Firestore firestore;

    @Mock
    private DocumentReference documentReference;

    @Mock
    private DocumentSnapshot documentSnapshot;

    @Mock
    private QuerySnapshot querySnapshot;

    @Mock
    private QueryDocumentSnapshot queryDocumentSnapshot;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(firebaseInitializer.getDb()).thenReturn(firestore);
        when(firestore.collection(anyString()).document(anyString())).thenReturn(documentReference);
        when(documentReference.get()).thenReturn(ApiFutures.immediateFuture(documentSnapshot));
        when(documentSnapshot.exists()).thenReturn(true);
        when(documentSnapshot.toObject(any())).thenReturn(new GroupFordetail());
    }

    @Test
    public void testGetGroupDetail() throws ExecutionException, InterruptedException {
        String documentId = "testDocumentId";

        GroupFordetail result = customControl.getGroupDetail(documentId);

        assertNotNull(result);
        assertEquals(documentId, result.getDocumentId());
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