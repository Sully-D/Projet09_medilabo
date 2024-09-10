package com.medilabo.frontend.controller;


import com.medilabo.frontend.service.NoteService;
import com.medilabo.note.model.Note;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;


@Controller
@Log4j2
public class PatientNotesController {

    @Autowired
    private NoteService noteService;

    @GetMapping("/patients/{patientId}/notes")
    public String getPatientNotes(@PathVariable String patientId, Model model) {
        log.info("Received request to get patient notes");

        Optional<List<Note>> notes = noteService.getNotesByPatientId(patientId);

        if (notes.isPresent()) {
            model.addAttribute("notes", notes.get());
            return "notes";
        } else {
            model.addAttribute("error", "No notes found for patient ID: " + patientId);
            return "error";
        }
    }

    @PostMapping("/patients/{patientId}/notes")
    public String addNote(@PathVariable String patientId, Note note) {
        log.info("Received request to add note: {}", note);
        noteService.addNote(note);
        return "redirect:/patients/" + patientId + "/notes";
    }

}
