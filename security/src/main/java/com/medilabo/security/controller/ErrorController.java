package com.medilabo.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class ErrorController {

    @RequestMapping("/custom-error")
    public String handleError() {
        // Renvoie la vue error.html
        return "error";
    }
}
