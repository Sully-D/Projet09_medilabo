package com.medilabo.frontend.controller;


import com.medilabo.frontend.model.Note;
import com.medilabo.frontend.service.NoteService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;


/**
 * Controller handling patient notes operations.
 * Retrieves and adds notes for a specific patient identified by their ID.
 */
@Controller
@Log4j2
public class PatientNotesController {

    @Autowired
    private NoteService noteService;

    /**
     * Retrieves notes for a specific patient identified by their ID.
     * @param patientId The ID of the patient whose notes are being retrieved.
     * @param model The model to which the retrieved notes will be added.
     * @return The name of the view template to render.
     */
    @GetMapping("/patients/{patientId}/notes")
    public String getPatientNotes(@PathVariable String patientId, Model model) {
        log.info("Received request to get patient notes");

        Optional<List<Note>> notes = Optional.ofNullable(noteService.getNotesByPatientId(patientId));

        if (notes.isPresent()) {
            model.addAttribute("notes", notes.get());
            return "notes";
        } else {
            model.addAttribute("error", "No notes found for patient ID: " + patientId);
            return "error";
        }
    }

    /**
     * Adds a new note for a specific patient identified by their ID.
     * @param patientId The ID of the patient whose note is being added.
     * @param note The note to be added.
     * @return The name of the view template to render.
     */
    @PostMapping("/patients/{patientId}/notes")
    public String addNote(@PathVariable String patientId, Note note) {
        log.info("Received request to add note: {}", note);
        noteService.addNote(note);
        return "redirect:/patients/" + patientId + "/notes";
    }

}
