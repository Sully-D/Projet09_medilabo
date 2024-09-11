package com.medilabo.note.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private String noteDate;

}
