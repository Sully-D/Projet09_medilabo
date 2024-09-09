package com.medilabo.note.service;

import com.medilabo.note.model.Note;

import java.util.List;

public interface NoteService {

    List<Note> getNotesByPatientId(String patientId);

    Note addNote(Note note);
}
