package com.medilabo.risklevel.service.impl;

import com.medilabo.note.model.Note;
import com.medilabo.risklevel.config.NoteServiceConfig;
import com.medilabo.risklevel.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;


@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private WebClient webclient;

    private NoteServiceConfig noteServiceConfig;

    @Autowired
    public NoteServiceImpl(NoteServiceConfig noteServiceConfig) {
        this.noteServiceConfig = noteServiceConfig;
    }

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
