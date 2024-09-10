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

    @GetMapping
    public Optional<List<Note>> getNotesByPatientId(@RequestParam String patientId) {
        return noteService.getNotesByPatientId(patientId);
    }

    @PostMapping
    public Note addNote(@RequestBody Note note) {
        return noteService.save(note);
    }
}
