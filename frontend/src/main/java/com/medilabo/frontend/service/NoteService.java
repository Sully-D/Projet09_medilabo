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


    public List<Note> getNotesByPatientId(String patientId) {
        return webClient.get()
                .uri(baseUrl + "?patientId=" + patientId)
                .retrieve()
                .bodyToFlux(Note.class)
                .collectList()
                .block();
    }

    public Note addNote(Note note) {
        return webClient.post()
                .uri(baseUrl)
                .bodyValue(note)
                .retrieve()
                .bodyToMono(Note.class)
                .block();
    }
}
