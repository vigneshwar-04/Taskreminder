package com.example.taskreminder.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password) {

        if (username.equals("admin") && password.equals("1234")) {
            return "Login Successful";
        } else {
            return "Invalid Credentials";
        }
    }
}