package com.medilabo.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;


@CrossOrigin(origins = "*")
@Controller
public class LoginController {

    @GetMapping("/auth/login")
    public String loginPage() {
        return "login";
    }
}