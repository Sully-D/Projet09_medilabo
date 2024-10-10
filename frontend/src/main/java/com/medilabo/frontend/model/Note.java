package com.medilabo.frontend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


@Data
public class Note {

    private String id;
    private String patientId;
    private String noteContent;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String noteDate;

}

