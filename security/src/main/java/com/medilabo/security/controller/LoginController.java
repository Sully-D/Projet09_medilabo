package com.medilabo.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;


@CrossOrigin(origins = "*")
@Controller
public class LoginController {

    /**
     * Retrieves the login page.
     *
     * @return The name of the login page.
     */
    @GetMapping("/auth/login")
    public String loginPage() {
        return "login";
    }
}