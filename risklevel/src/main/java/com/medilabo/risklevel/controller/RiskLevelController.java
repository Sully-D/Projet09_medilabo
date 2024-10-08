package com.medilabo.risklevel.controller;

import com.medilabo.backend.model.Patient;
import com.medilabo.note.model.Note;
import com.medilabo.risklevel.service.NoteService;
import com.medilabo.risklevel.service.PatientService;
import com.medilabo.risklevel.service.ResultatAnalysis;
import com.medilabo.risklevel.service.RiskAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Controller class for managing risk levels.
 * Retrieves patient information and notes to analyze the risk level.
 * Uses services for note retrieval, patient retrieval, risk analysis, and result analysis.
 */
@RestController
@RequestMapping("/api/risklevels")
public class RiskLevelController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private ResultatAnalysis resultatAnalysis;

    @Autowired
    private RiskAnalysis riskAnalysis;

    /**
     * Controller class for managing risk levels.
     * Retrieves patient information and notes to analyze the risk level.
     * Utilizes internal services for note retrieval, patient retrieval, risk analysis, and result analysis.
     */
    @GetMapping
    public String getRiskLevel(@RequestParam String patientId) {

        Patient patient = patientService.getPatient(patientId);
        List<Note> notes = noteService.getNoteByPatientId(patientId);

        int nbSymptoms = riskAnalysis.searchForSymptoms(notes);
        String levelOfRisk = resultatAnalysis.levelOfRisk(nbSymptoms, patient);

        return levelOfRisk;
    }
}
