package com.medilabo.frontend.controller;

import com.medilabo.frontend.service.RisklevelService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@Log4j2
public class PatientRisklevelController {

    @Autowired
    private RisklevelService risklevelService;

    @GetMapping("/risklevel/{id}")
    public String getRiskLevelForPatient(@PathVariable String id, Model model) {
        log.info("Received request to get risk level for patient ID: {}", id);

        try {
            String riskLevel = risklevelService.getRiskLevelForPatient(id);
            model.addAttribute("riskLevel", riskLevel);
            return "risklevel-patient";

        } catch (Exception e) {
            log.error("Error occurred while fetching risk level for patient ID: {}", id, e);
            model.addAttribute("error", "An error occurred while fetching the risk level");
            return "error";
        }
    }

}
