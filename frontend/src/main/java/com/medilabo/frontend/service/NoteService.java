package com.medilabo.frontend.service;

import com.medilabo.note.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Service class for handling note-related operations.
 * Utilizes WebClient for making HTTP requests to an external note service.
 */
@Service
public interface NoteService {

    /**
     * Retrieves a list of notes for a specific patient by their patient ID.
     * This method makes a GET request to an external service using WebClient to fetch the notes.
     *
     * @param patientId The ID of the patient whose notes are being retrieved.
     * @return A list of Note objects associated with the patient.
     */
    List<Note> getNotesByPatientId(String patientId);


    /**
     * Adds a new note by sending a POST request to the external service.
     * The note is passed as the body of the request, and the response contains the created note.
     *
     * @param note The Note object to be added.
     * @return The created Note object, including any modifications (e.g., ID) made by the external service.
     */
    Note addNote(Note note);

}
