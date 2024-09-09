package com.medilabo.note.repository;

import com.medilabo.note.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends MongoRepository<Note, String> {

    Optional<List<Note>> findByPatientId(String patientId);
}
