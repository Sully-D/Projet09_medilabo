package com.medilabo.frontend.service;

import com.medilabo.note.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    @Value("${note.service.base-url}")
    private String baseUrl; // Inject URL from application.yml

    @Autowired
    private WebClient webClient;


    /**
     * Retrieves a list of notes for a specific patient by their patient ID.
     * This method makes a GET request to an external service using WebClient to fetch the notes.
     *
     * @param patientId The ID of the patient whose notes are being retrieved.
     * @return A list of Note objects associated with the patient.
     */
    public List<Note> getNotesByPatientId(String patientId) {
        // Send a GET request to the external service with the patientId as a query parameter
        return webClient.get()
                .uri(baseUrl + "?patientId=" + patientId) // Construct the URI with the patient ID as a query parameter
                .retrieve() // Trigger the request and retrieve the response
                .bodyToFlux(Note.class) // Map the response body to a flux of Note objects
                .collectList() // Collect the flux into a List of Note objects
                .block(); // Block until the result is available and return the list
    }


    /**
     * Adds a new note by sending a POST request to the external service.
     * The note is passed as the body of the request, and the response contains the created note.
     *
     * @param note The Note object to be added.
     * @return The created Note object, including any modifications (e.g., ID) made by the external service.
     */
    public Note addNote(Note note) {
        // Send a POST request to the external service to add a new note
        return webClient.post()
                .uri(baseUrl) // The base URL for the external service where the note will be sent
                .bodyValue(note) // Attach the note object as the body of the POST request
                .retrieve() // Trigger the request and retrieve the response
                .bodyToMono(Note.class) // Map the response body to a Mono of Note object
                .block(); // Block until the result is available and return the created Note object
    }

}
