package com.oop.projectmanagement.controller;

import com.google.cloud.firestore.*;
import com.oop.projectmanagement.FirebaseInitializer;
import com.oop.projectmanagement.model.GroupFordetail;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.ui.Model;
import org.springframework.mock.web.MockHttpSession;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GroupDetailTest {

    @InjectMocks
    private GroupDetail groupDetail;

    @Mock
    private FirebaseInitializer firebaseInitializer;

    @Mock
    private Firestore firestore;

    @Mock
    private DocumentReference documentReference;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(firebaseInitializer.getDb()).thenReturn(firestore);
        when(firestore.collection(anyString()).document(anyString())).thenReturn(documentReference);
    }

    @Test
    public void testGetUserinfo() throws ExecutionException, InterruptedException {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "testUser");
        session.setAttribute("firstName", "Test");
        session.setAttribute("lastName", "User");
        String documentId = "testDocumentId";
        Model model = mock(Model.class);

        String result = groupDetail.getUserinfo(session, documentId, model);

        assertEquals("groupdetail", result);
        verify(model, times(1)).addAttribute(eq("group"), any(GroupFordetail.class));
    }

    @Test
    public void testDeleteGroup() throws ExecutionException, InterruptedException {
        String documentId = "testDocumentId";

        String result = groupDetail.deleteGroup(documentId);

        assertEquals("Group deleted successfully", result);
    }

    @Test
    public void testEditGroup() {
        String documentId = "testDocumentId";
        String groupName = "testGroupName";
        String groupDescription = "testGroupDescription";

        String result = groupDetail.editGroup(documentId, groupName, groupDescription);

        assertEquals("Group edited successfully", result);
    }
}
