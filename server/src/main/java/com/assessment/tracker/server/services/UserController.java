package com.assessment.tracker.server.services;
import com.assessment.tracker.server.persistence.user.User;
import com.assessment.tracker.server.persistence.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/users") //corresponding to service

//rerun application after every change
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    private String getUsernameByEmailOrUsername(String identifier){
        User user = userService.getUserByEmail(identifier);
        if(user == null){
            user = userService.getUserByUsername(identifier);
        }
        return user.getUsername();

        //helper method to extract user based on either email or username
    }


    //implement CRUD operations
    // -------------------- CREATE --------------------
    @PostMapping( "/")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    //-------------------- READ --------------------
    //implement get users w/o query params
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
       List<User> users = userService.getAllUsers();
        return(users != null)? ResponseEntity.ok(users)
                : ResponseEntity.notFound().build();
    }

    //implement get user and get all users w query params
    @GetMapping("/un/{username}") // separate routing for username to avoid ambiguity
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return (user != null) ? ResponseEntity.ok(user)
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/email/{email}") // separate routing for email to avoid ambiguity
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return (user != null) ? ResponseEntity.ok(user)
                : ResponseEntity.notFound().build();

    }

    //separate routing to avoid ambiguity
    @GetMapping("/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        User user = userService.getUser(id);
        return (user != null) ? ResponseEntity.ok(user)
                : ResponseEntity.notFound().build();
    }

    //Implement update user (implement change to update using DTOs)
    //-------------------- UPDATE --------------------
    @PutMapping("/{id}/password")
    public ResponseEntity<User> updateUserPassword(@PathVariable int id, @RequestBody User updatedUser) {
        User existing = userService.getUser(id);
        if (existing == null) return ResponseEntity.notFound().build();

        User updated = userService.updateUserPassword(updatedUser.getPassword(), id);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/email")
    public ResponseEntity<User> updateUserEmail(@PathVariable int id, @RequestBody User updatedUser) {
        User existing = userService.getUser(id);
        if (existing == null) return ResponseEntity.notFound().build();

        User updated = userService.updateUserEmail(updatedUser.getEmail(), id);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/username")
    public ResponseEntity<User> updateUsername(@PathVariable int id, @RequestBody User updatedUser) {
        User existing = userService.getUser(id);
        if (existing == null) return ResponseEntity.notFound().build();

        User updated = userService.updateUsername(updatedUser.getUsername(), id);
        return ResponseEntity.ok(updated);
    }


    //-------------------- DELETE --------------------
    //implement delete user by id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        boolean deleted = (userService.deleteUser(id));
        if (!deleted) {
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("User deleted successfully.", HttpStatus.OK);
    }

    //implement delete all users :)
    @DeleteMapping("/wipe")
    public ResponseEntity<String> deleteAllUsers(@RequestParam String confirm){
        boolean deleted= userService.deleteAllUsers();
        if (!"YUTA_BUM".equals(confirm)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid confirmation value. Action not performed.");
        }
        userService.deleteAllUsers();
        return new ResponseEntity<>("All Users Erased", HttpStatus.OK);
    }





}
