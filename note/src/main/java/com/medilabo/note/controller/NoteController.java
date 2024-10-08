package com.medilabo.note.controller;

import com.medilabo.note.model.Note;
import com.medilabo.note.service.NoteService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/notes")
@Log4j2
public class NoteController {

    @Autowired
    private NoteService noteService;

    /**
     * Retrieves a list of notes based on the provided patient ID.
     *
     * @param patientId The ID of the patient for whom notes are being retrieved
     * @return An optional containing a list of notes associated with the patient ID, or empty if no notes are found
     */
    @GetMapping
    public Optional<List<Note>> getNotesByPatientId(@RequestParam String patientId) {
        return noteService.getNotesByPatientId(patientId);
    }

    /**
     * Adds a new note to the system.
     *
     * @param note The note object to be added
     * @return The saved note object
     */
    @PostMapping
    public Note addNote(@RequestBody Note note) {
        return noteService.save(note);
    }
}
