package com.oop.projectmanagement;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.Firestore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;



import org.springframework.stereotype.Service;


@Service
    public class FirebaseInitializer {
        public void initialize() {
            try {
               //file path to the service account key json file
               File file = new File(ClassLoader.getSystemResource("serviceAccountKey.json").getFile());

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(new FileInputStream(file)))
                        .build();

                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
     public Firestore getDb() {
            initialize();
        return FirestoreClient.getFirestore();}
}

   
