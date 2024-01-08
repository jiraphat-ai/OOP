package com.oop.projectmanagement.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@PropertySource(value = "CLASS_PATH:application.properties")
public class FirebaseConfig {

    @Value("${firebase.config.path}")
    private String firebaseConfigPath;

    public void initializeFirebase() throws IOException {
        FileInputStream serviceAccount = new FileInputStream(firebaseConfigPath);

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://projectmanagement93-cda75.firebaseio.com") // Replace with your Firebase project URL
                .build();

        FirebaseApp.initializeApp(options);
    }
}
