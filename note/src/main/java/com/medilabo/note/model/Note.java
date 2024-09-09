package com.medilabo.note.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "note")
@Data
public class Note {

    @Id
    private String id;
    private String patientId;
    private String noteContent;
    private String noteDate;
}
