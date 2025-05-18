package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Users;
import com.example.demo.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class LoginController {

    LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/")
    public String hello() {
        return "hello";
    }

    @PostMapping("/signin")
    public String Signin(@RequestBody Users user) {
        System.out.println(user + "these are login details");
        return loginService.Signin(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody Users user) {
        return loginService.login(user);
    }

    @GetMapping("/getAll")
    public List<Users> getAll() {
        return loginService.getAll();
    }

    @GetMapping("/get-user-details")
    public Users getUserDetails(HttpServletRequest request) {
        return loginService.getUserDetails(request);
    }

    @PutMapping("/update-user-details")
    public Map<String, Object> updateUserDetails(@RequestBody Users user) {
        return loginService.updateUserDetails(user);
    }
}
