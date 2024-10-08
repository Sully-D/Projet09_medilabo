package com.medilabo.frontend.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller handling error pages.
 * This controller is responsible for mapping the
 * {@code /error} route to the error page.
 *
 */
public class ErrorController {
    @GetMapping("/error")
    public String handleError(Model model) {
        model.addAttribute("errorMessage", "Une erreur s'est produite lors du traitement de votre demande.");
        return "error";
    }
}
