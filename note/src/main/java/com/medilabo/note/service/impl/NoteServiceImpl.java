package com.medilabo.note.service.impl;

import com.medilabo.note.model.Note;
import com.medilabo.note.repository.NoteRepository;
import com.medilabo.note.service.NoteService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Log4j2
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;

    /**
     * Retrieves a list of notes associated with a specific patient ID.
     *
     * @param patientId The ID of the patient to retrieve notes for
     * @return An Optional containing the list of notes if found, empty otherwise
     * @throws IllegalArgumentException if the patientId is null
     */
    @Override
    public Optional<List<Note>> getNotesByPatientId(String patientId) {

        if (patientId == null) {
            throw new IllegalArgumentException("Patient ID cannot be null");
        }

        Optional<List<Note>> notes = noteRepository.findByPatientId(patientId);

        if (notes.isPresent()) {
            log.info("Notes found for patient ID: {}", patientId);
        } else {
            log.warn("No notes found for patient ID: {}", patientId);
        }

        return notes;
    }

    /**
     * Saves a note.
     *
     * @param note the note to be saved
     * @return the saved note
     */
    @Override
    public Note save(Note note) {
        if (note == null) {
            throw new IllegalArgumentException("Note cannot be null");
        }

        return noteRepository.save(note);
    }
}
