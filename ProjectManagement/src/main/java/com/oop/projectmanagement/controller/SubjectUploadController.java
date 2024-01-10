package com.oop.projectmanagement.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.firestore.Firestore;
import com.oop.projectmanagement.FirebaseInitializer;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

@RestController
public class SubjectUploadController {
    FirebaseInitializer firebaseInitializer = new FirebaseInitializer();
      @PostMapping("/uploadsubject")
    public void uploadSubjectFile(@RequestParam("file") MultipartFile file) {
                        System.out.println("test1");

        // validate file
        if (file.isEmpty()) {
            throw new RuntimeException("Please select a CSV file to upload.");
        } else {

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            // create csv bean reader
            CsvToBean<Subject> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(Subject.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            // convert `CsvToBean` object to list of users
            List<Subject> users = csvToBean.parse();

            // TODO: save users in DB?

            
            //save users to firestore
            Firestore db = firebaseInitializer.getDb();
            for (Subject user : users) {
                db.collection("subject").add(user);
            }
            //read each user
            for (Subject user : users) {
                System.out.println(user.getSubjectID());
            }

            System.out.println("CSV file was uploaded and parsed successfully.");

        } catch (Exception ex) {
            throw new RuntimeException("Failed to parse CSV file: " + ex.getMessage());
        }
    }
    }
}
