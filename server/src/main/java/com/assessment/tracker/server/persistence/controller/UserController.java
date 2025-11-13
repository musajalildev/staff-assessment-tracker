package com.assessment.tracker.server.persistence.controller;
import com.assessment.tracker.server.persistence.domain.User;
import com.assessment.tracker.server.services.UserService;

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

    @PostMapping({"", "/"})
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    //implement get users w/o query params
    @GetMapping({"/"})
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    //implement get user and get all users w query params
    @GetMapping({"/un/{username}"}) // separate routing for username to avoid ambiguity
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
    }

    @GetMapping({"/email/{email}"})
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }

    //separate routing to avoid ambiguity
    @GetMapping("/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        User user = userService.getUser(id);
        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //implement update user
    @PostMapping("/id/{id}")


    @DeleteMapping({"/{id}"})
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }




}
