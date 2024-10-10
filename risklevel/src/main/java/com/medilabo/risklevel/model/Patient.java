package com.medilabo.risklevel.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Patient {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    private String firstName;

    @NotNull
    @Size(min = 1, max = 50)
    private String lastName;

    @NotNull
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$") // Format YYYY-MM-DD
    private String dateOfBirth;

    @NotNull
    @Pattern(regexp = "Male|Female")
    private String gender;

    private String postalAddress;

    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$")
    private String phone;
}