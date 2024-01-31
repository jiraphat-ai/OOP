package com.oop.projectmanagement.controller;

import com.oop.projectmanagement.model.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RestController;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.google.cloud.firestore.Firestore;
import com.oop.projectmanagement.FirebaseInitializer;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

@RestController
public class FileUploadController {
    FirebaseInitializer firebaseInitializer = new FirebaseInitializer();
    @PostMapping("/upload")
    public void uploadCSVFile(@RequestParam("file") MultipartFile file) {
        // validate file
        if (file.isEmpty()) {
            throw new RuntimeException("Please select a CSV file to upload.");
        } else {

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            // create csv bean reader
            CsvToBean<User> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(User.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();


            // convert `CsvToBean` object to list of users
            List<User> users = csvToBean.parse();

            // TODO: save users in DB?


            //save users to firestore
            Firestore db = firebaseInitializer.getDb();
            for (User user : users) {
                //set facebook and instagram and bio to null
                user.setFacebook(null);
                user.setInstagram(null);
                user.setBio(null);
                ArrayList<String> tag = new ArrayList<String>();
                user.setTag(tag);
                db.collection("useraccount").add(user);
            }
            //read each user
            for (User user : users) {
                System.out.println(user.getUsername());
            }

            System.out.println("CSV file was uploaded and parsed successfully.");

        } catch (Exception ex) {
            throw new RuntimeException("Failed to parse CSV file: " + ex.getMessage());
        }
    }
}


  }
    