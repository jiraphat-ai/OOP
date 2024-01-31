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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;



@WebMvcTest(GroupDetail.class)
public class GroupDetailTest {
    @InjectMocks
    private GroupDetail groupDetail;

    @Mock
    private FirebaseInitializer firebaseInitializer;

    @Mock
    private Firestore firestore;

    @Mock
    private DocumentReference documentReference;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(firebaseInitializer.getDb()).thenReturn(firestore);
        when(firestore.collection(anyString()).document(anyString())).thenReturn(documentReference);
    }

    @Test
    public void getUserinfo() throws Exception {
        this.mockMvc.perform(get("/groupdetail")
          .param("documentId", "abc"))
            .andExpect(status().isOk())
            .andExpect(view().name(""))
            .andExpect(model().attributeExists("<name>"))
            .andExpect(model().attribute("<name>", "<value>"))
            .andExpect(content().string(""));
    }
}
