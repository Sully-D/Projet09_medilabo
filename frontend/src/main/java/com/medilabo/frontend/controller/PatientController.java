package com.medilabo.frontend.controller;

import com.medilabo.backend.model.Patient;
import com.medilabo.frontend.service.NoteService;
import com.medilabo.frontend.service.PatientService;
import com.medilabo.note.model.Note;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


/**
 * Controller handling the display of patient-related pages and interactions.
 */
@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/patients")
public class PatientController {

    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);

    @Autowired
    private PatientService patientService;

    @Autowired
    private NoteService noteService;

    /**
     * Displays the list of all patients.
     *
     * @param model The model to which the list of patients will be added.
     * @return The name of the view template to render.
     */
    @GetMapping()
    public String listPatients(Model model) {
        logger.info("Received request to display all patients");

        try {
            List<Patient> patients = patientService.getAllPatients();
            model.addAttribute("patients", patients);
            return "patient";

        } catch (Exception e) {
            logger.error("Error occurred while fetching patients: {}", e.getMessage(), e);
            model.addAttribute("error", "An error occurred while fetching patients");
            return "error";
        }
    }

    /**
     * Displays the form to add a new patient.
     *
     * @param model The model to which the new patient form will be added.
     * @return The name of the view template to render.
     */
    @GetMapping("/add")
    public String showAddPatientForm(Model model) {
        logger.info("Received request to display the add patient form");
        model.addAttribute("patient", new Patient());
        return "new-patient";
    }

    /**
     * Handles the submission of the form to add a new patient.
     *
     * @param patient The patient object to be added.
     * @return A redirect to the list of patients.
     */
    @PostMapping("/add")
    public String addPatient(@ModelAttribute Patient patient, @RequestParam("noteContent") String noteContent) {
        logger.info("Received request to add a new patient: {}", patient);

        try {
            Patient savedPatient = patientService.createPatient(patient);

            // If notes are added, save them too
            if (!noteContent.isEmpty()) {
                Note note = new Note();
                note.setPatientId(savedPatient.getId());  // Link the note to the patient
                note.setNoteContent(noteContent);
                note.setNoteDate(String.valueOf(System.currentTimeMillis()));
                noteService.addNote(note);
            }

            return "redirect:/patients";

        } catch (Exception e) {
            logger.error("Error occurred while adding patient: {}", e.getMessage(), e);
            return "redirect:/patients/new-patient?error=true";
        }
    }

    /**
     * Displays the form to update an existing patient.
     *
     * @param id The ID of the patient to be updated.
     * @param model The model to which the patient form will be added.
     * @return The name of the view template to render.
     */
    @GetMapping("/edit/{id}")
    public String showEditPatientForm(@PathVariable String id, Model model) {
        logger.info("Received request to display the edit patient form for ID: {}", id);

        try {
            // Retrieve patient by ID
            Patient patient = patientService.getPatientById(id);

            // Retrieve patient notes by ID
            List<Note> notes = noteService.getNotesByPatientId(id).orElse(Collections.emptyList());

            // Add patient and notes to template
            model.addAttribute("patient", patient);
            model.addAttribute("notes", notes);

            return "edit-patient";

        } catch (Exception e) {
            logger.error("Error occurred while fetching patient or notes with ID: {}", id, e);
            model.addAttribute("error", "An error occurred while fetching the patient or notes");
            return "error";
        }
    }


    /**
     * Handles the submission of the form to update an existing patient.
     *
     * @param id The ID of the patient to be updated.
     * @param patient The updated patient object.
     * @return A redirect to the list of patients.
     */
    @PostMapping("/edit/{id}")
    public String updatePatient(@PathVariable String id, @ModelAttribute Patient patient) {
        logger.info("Received request to update patient with ID: {}", id);

        try {
            patient.setId(id);
            patientService.updatePatient(patient);
            return "redirect:/patients";

        } catch (Exception e) {
            logger.error("Error occurred while updating patient with ID: {}", id, e);
            return "redirect:/patients/edit/" + id + "?error=true";
        }
    }

    /**
     * Deletes a patient and redirects to the list of patients.
     *
     * @param id The ID of the patient to be deleted.
     * @return A redirect to the list of patients.
     */
    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable String id) {
        logger.info("Received request to delete patient with ID: {}", id);

        try {
            patientService.deletePatient(id);
            return "redirect:/patients";

        } catch (Exception e) {
            logger.error("Error occurred while deleting patient with ID: {}", id, e);
            return "redirect:/patients?error=true";
        }
    }
}