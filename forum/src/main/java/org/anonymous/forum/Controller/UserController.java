package org.anonymous.forum.Controller;

import org.anonymous.forum.Entity.User;
import org.anonymous.forum.Enum.Gender;
import org.anonymous.forum.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/profile")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> requestBody) {
        String id = requestBody.get("id");
        String password = requestBody.get("password");
        String genderStr = requestBody.get("gender");
        Gender gender = genderStr == null ? Gender.SECRET : Gender.valueOf(genderStr);
        if (id == null || id.isEmpty() || password == null || password.isEmpty()) {
            return ResponseEntity.badRequest().body("Username or password null.");
        }
        if (userService.addUser(id, password, gender)) {
            return ResponseEntity.ok().body("User registered.");
        } else {
            return ResponseEntity.badRequest().body("User already exists.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> requestBody) {
        String id = requestBody.get("id");
        String password = requestBody.get("password");
        if (userService.checkPassword(id, password)) {
            return ResponseEntity.ok().body("User logged in.");
        }
        return ResponseEntity.badRequest().body("Invalid username or password.");
    }

    @PostMapping("/updateId")
    public ResponseEntity<String> updateId(@RequestBody Map<String, String> requestBody) {
        String oldId = requestBody.get("oldId");
        String newId = requestBody.get("newId");
        if (newId == null || newId.isEmpty()) {
            return ResponseEntity.badRequest().body("newId null.");
        }
        if (userService.updateId(oldId, newId)) {
            return ResponseEntity.ok().body("Username updated.");
        } else {
            return ResponseEntity.badRequest().body("Username invalid.");
        }
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody Map<String, String> requestBody) {
        String id = requestBody.get("id");
        String oldPassword = requestBody.get("oldPassword");
        String newPassword = requestBody.get("newPassword");
        if (newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.badRequest().body("newPassword null.");
        }
        if (userService.updatePassword(id, oldPassword, newPassword)) {
            return ResponseEntity.ok().body("Password updated.");
        } else {
            return ResponseEntity.badRequest().body("Username or password incorrect.");
        }
    }

    @PostMapping("/updateGender")
    public ResponseEntity<String> updateGender(@RequestBody Map<String, String> requestBody) {
        String id = requestBody.get("id");
        String genderStr = requestBody.get("gender");
        Gender gender = genderStr == null ? Gender.SECRET : Gender.valueOf(genderStr);
        userService.updateGender(id, gender);
        return ResponseEntity.ok().body("Password updated.");
    }

    @PostMapping("/getUser")
    public ResponseEntity<User> getUser(@RequestBody Map<String, String> requestBody) {
        String id = requestBody.get("id");
        User user = userService.getUser(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(user);
    }
}
