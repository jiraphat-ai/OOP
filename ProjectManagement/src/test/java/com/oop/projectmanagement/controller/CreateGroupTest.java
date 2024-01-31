package com.oop.projectmanagement.controller;

import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.*;
import com.oop.projectmanagement.FirebaseInitializer;
import com.oop.projectmanagement.model.Group;
import samples.newmocking.SomeDependency;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.mock.web.MockHttpSession;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;



@WebMvcTest(CreateGroup.class)
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

    @MockBean
    private SomeDependency someDependency;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(firebaseInitializer.getDb()).thenReturn(firestore);
        when(firestore.collection(anyString())).thenReturn(collectionReference);
        when(collectionReference.add(any(Group.class))).thenReturn(ApiFutures.immediateFuture(documentReference));
        when(documentReference.get()).thenReturn(ApiFutures.immediateFuture(mock(DocumentSnapshot.class)));
    }


    @Test
    public void getUserinfo() throws Exception {
        this.mockMvc.perform(get("/creategroup"))
            .andExpect(status().isOk())
            .andExpect(view().name(""))
            .andExpect(model().attributeExists("<name>"))
            .andExpect(model().attribute("<name>", "<value>"))
            .andExpect(content().string(""));
    }

    @Test
    public void createGroup() throws Exception {
        this.mockMvc.perform(post("/createGroup"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("<name>"))
            .andExpect(model().attribute("<name>", "<value>"))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.<key>").value("<value>"));
    }

    @Test
    public void testGetSubjectID() throws Exception {
        CreateGroup createGroup = new CreateGroup();
        Method method = CreateGroup.class.getDeclaredMethod("getSubjectID");
        method.setAccessible(true);
        List<Map<String, Object>> result = (List<Map<String, Object>>) method.invoke(createGroup);
        // assertions...
    }
}
