package com.medilabo.note.service;

import com.medilabo.note.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface NoteService {

    Optional<List<Note>> getNotesByPatientId(String patientId);

    Note addNote(Note note);
}
