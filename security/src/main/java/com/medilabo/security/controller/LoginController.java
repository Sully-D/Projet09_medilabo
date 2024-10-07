package com.medilabo.security.controller;

import com.medilabo.security.service.JWTService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*")
@RestController
public class LoginController {


    private JWTService jwtService;

    public LoginController(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> getToken(Authentication authentication) {
        String token = jwtService.generateToken(authentication);

        // Log pour vérifier que le token est généré correctement
        System.out.println("JWT Token généré : " + token);

        // Ajouter le JWT dans l'en-tête 'Authorization'
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        return ResponseEntity.ok().headers(headers).body("Login successful, token attached in header");
    }

}