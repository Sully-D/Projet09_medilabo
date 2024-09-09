package com.medilabo.note.controller;

import com.medilabo.note.model.Note;
import com.medilabo.note.service.NoteService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
@Log4j2
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping("/{patientId}")
    public ResponseEntity<Optional<List<Note>>> getNotesByPatient(@PathVariable String patientId) {
        Optional<List<Note>> notes = noteService.getNotesByPatientId(patientId);
        return ResponseEntity.ok(notes);
    }

    @PostMapping
    public ResponseEntity<Note> addNote(@RequestBody Note note) {
        Note savedNote = noteService.addNote(note);
        return ResponseEntity.ok(savedNote);
    }
}
