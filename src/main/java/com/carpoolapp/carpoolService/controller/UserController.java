package com.carpoolapp.carpoolService.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/{id}")
    public String getUserById(@PathVariable String id) {
        // Return a simple string response for testing
        return "User with ID " + id + " is a test user1.";
    }
}
