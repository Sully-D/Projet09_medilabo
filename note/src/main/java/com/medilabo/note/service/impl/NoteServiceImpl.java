package com.medilabo.note.service.impl;

import com.medilabo.note.model.Note;
import com.medilabo.note.repository.NoteRepository;
import com.medilabo.note.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Override
    public List<Note> getNotesByPatientId(String patientId) {

        if (patientId == null) {
            throw new IllegalArgumentException("Patient ID cannot be null");
        }

        Optional<List<Note>> notes = noteRepository.findByPatientId(patientId);

        if (notes.isPresent()) {
            return notes.get();
        } else {
            return List.of();
        }
    }

    @Override
    public Note addNote(Note note) {

        if (note == null) {
            throw new IllegalArgumentException("Note cannot be null");
        }

        return noteRepository.save(note);
    }
}
