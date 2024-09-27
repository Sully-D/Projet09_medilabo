package com.medilabo.risklevel.service.impl;

import com.medilabo.note.model.Note;
import com.medilabo.risklevel.config.NoteServiceConfig;
import com.medilabo.risklevel.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;


/**
 * Service implementation for handling notes with patient-specific operations.
 * Utilizes WebClient for making HTTP requests to the note service based on the provided configuration.
 */
@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private WebClient webclient;

    private NoteServiceConfig noteServiceConfig;

    @Autowired
    public NoteServiceImpl(NoteServiceConfig noteServiceConfig) {
        this.noteServiceConfig = noteServiceConfig;
    }

    /**
     * Retrieves a list of notes specific to a given patient ID by making an HTTP GET request using WebClient.
     * The URI is constructed based on the base URL provided in the NoteServiceConfig along with the patient ID as a query parameter.
     * Converts the response body to a Flux of Note objects, collects them into a list, and then blocks until the list is available.
     *
     * @param patientId the ID of the patient for whom notes are being retrieved
     * @return a list of Note objects associated with the specified patient ID
     */
    @Override
    public List<Note> getNoteByPatientId(String patientId) {
        return webclient.get()
                .uri(noteServiceConfig.getBaseUrl() + "?patientId=" + patientId)
                .retrieve()
                .bodyToFlux(Note.class)
                .collectList()
                .block();

    }
}
