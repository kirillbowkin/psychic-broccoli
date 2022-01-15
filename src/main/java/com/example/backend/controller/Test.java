package com.example.backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/userRole")
    public String userRole() {
        return "this controller is for user role";
    }

}
