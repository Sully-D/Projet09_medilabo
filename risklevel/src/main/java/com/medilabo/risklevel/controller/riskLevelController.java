package com.medilabo.risklevel.controller;

import com.medilabo.backend.model.Patient;
import com.medilabo.note.model.Note;
import com.medilabo.risklevel.service.NoteService;
import com.medilabo.risklevel.service.PatientService;
import com.medilabo.risklevel.service.ResultatAnalysis;
import com.medilabo.risklevel.service.RiskAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/risklevel")
public class riskLevelController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private ResultatAnalysis resultatAnalysis;

    @Autowired
    private RiskAnalysis riskAnalysis;

    @GetMapping
    public ResponseEntity<String> getRiskLevel(@PathVariable String patientId) {

        List<Patient> patients = patientService.getPatient(patientId);
        List<Note> notes = noteService.getNoteByPatientId(patientId);

        int nbSymptoms = riskAnalysis.searchForSymptoms(notes);
        String levelOfRisk = resultatAnalysis.levelOfRisk(nbSymptoms, patients.get(0));

        return ResponseEntity.ok(levelOfRisk);
    }
}
