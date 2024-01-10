package com.oop.projectmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import javax.servlet.http.HttpSession;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.oop.projectmanagement.FirebaseInitializer;

@Controller
public class StudentHomeController {

    @Autowired
    private FirebaseInitializer firebaseInitializer;

    @GetMapping("/homestudent")
public String homestudent(HttpSession session, Model model) {
    String username = (String) session.getAttribute("username");
    Firestore db = firebaseInitializer.getDb();
    try {
        ApiFuture<QuerySnapshot> query = db.collection("useraccount").whereEqualTo("username", username).get();
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        if (!documents.isEmpty()) {
            DocumentSnapshot document = documents.get(0);
            // Fetch the values you need from the document
            String firstName = document.getString("firstname");
            String lastName = document.getString("lastname");
            // Add the values to the model
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);
            model.addAttribute("username", username);
        }
    } catch (Exception e) {
        model.addAttribute("error", "An error occurred: " + e.getMessage());
    }
    return "homestudent"; // Return the name of the view
}

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
@Controller
public class UserController {

    @GetMapping("/user")
    public String getUser(Model model) {
        User user = new User();
        // Set user attributes here
        model.addAttribute("user", user);
        return "user"; // This should be the name of your Thymeleaf template (without the .html extension)
    }

    @PostMapping("/user")
    public String postUser(@RequestParam("firstname") String firstname, @RequestParam("lastname") String lastname, Model model) {
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        model.addAttribute("user", user);
         return "redirect:/user";
    }
}
}