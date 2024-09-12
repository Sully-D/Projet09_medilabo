package com.medilabo.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Entity
@Table(name = "patients") // Table SQL
@Data
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Remplacer par un Long auto-généré

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