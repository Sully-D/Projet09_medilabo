package com.medilabo.frontend.controller;

import com.medilabo.frontend.model.Note;
import com.medilabo.frontend.model.Patient;
import com.medilabo.frontend.service.NoteService;
import com.medilabo.frontend.service.PatientService;
import com.medilabo.frontend.service.RisklevelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


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

    @Autowired
    private RisklevelService risklevelService;

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
                note.setPatientId(String.valueOf(savedPatient.getId()));  // Link the note to the patient
                note.setNoteContent(noteContent);
                note.setNoteDate(String.valueOf(OffsetDateTime.now()));
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
            List<Note> notes = noteService.getNotesByPatientId(id);
            logger.info("Notes fetched: {}", notes);

            // Retrieve patient level of risk by ID
            String levelOfRisk = risklevelService.getRiskLevelForPatient(id);
            logger.info("Level of risk fetched: {}", levelOfRisk);

            // Add patient, notes and level of risk to template
            model.addAttribute("patient", patient);
            model.addAttribute("notes", notes);
            model.addAttribute("riskLevel", levelOfRisk);

            return "edit-patient";

        } catch (Exception e) {
            logger.error("Error occurred while fetching patient or notes with ID: {}", id, e);
            model.addAttribute("error", "An error occurred while fetching the patient or notes");
            return "error";
        }
    }


    /**
     * Handles the submission of the form to update an existing patient.
     * It receives the patient's updated information and updates the record in the database.
     *
     * @param id      The ID of the patient to be updated. This is received from the URL path.
     * @param patient The updated patient object, populated from the form submission.
     * @return A redirect to the list of patients if successful, or back to the edit page with an error flag if the update fails.
     */
    @PostMapping("/edit/{id}")
    public String updatePatient(@PathVariable String id, @ModelAttribute Patient patient) {
        // Log the incoming update request for the patient
        logger.info("Received request to update patient with ID: {}", id);

        try {
            // Ensure that the patient object has the correct ID before updating
            patient.setId(Long.valueOf(id));

            // Call the service to perform the update in the database
            patientService.updatePatient(patient);

            // On successful update, redirect to the patients listing page
            return "redirect:/patients";

        } catch (Exception e) {
            // Log the error details if something goes wrong during the update
            logger.error("Error occurred while updating patient with ID: {}", id, e);

            // On failure, redirect back to the edit page and append an error query parameter
            return "redirect:/patients/edit/" + id + "?error=true";
        }
    }


    /**
     * Adds a new note to the specified patient.
     * If the note content is not empty, a new note is created and associated with the patient.
     *
     * @param id          The ID of the patient to whom the note is being added.
     * @param noteContent The content of the note being added.
     * @return A redirect back to the patient's edit page.
     */
    @PostMapping("/edit/{id}/add-note")
    public String addNoteToPatient(@PathVariable String id, @RequestParam("noteContent") String noteContent) {
        // Check if the note content is not empty before proceeding
        if (!noteContent.isEmpty()) {
            // Create a new Note object and associate it with the patient
            Note note = new Note();
            note.setPatientId(id);
            note.setNoteContent(noteContent);

            // Format the current date in 'yyyy-MM-dd' format and set it as the note's date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            note.setNoteDate(OffsetDateTime.now().format(formatter));

            // Save the new note via the NoteService
            noteService.addNote(note);
        }

        // Redirect back to the edit page of the patient after adding the note
        return "redirect:/patients/edit/" + id;
    }


    /**
     * Deletes a patient by their ID and redirects to the list of patients.
     * This method is triggered by a GET request and attempts to remove the patient
     * from the database. In case of an error, it redirects to the list with an error flag.
     *
     * @param id The ID of the patient to be deleted.
     * @return A redirect to the list of patients if successful, or with an error flag in case of failure.
     */
    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable String id) {
        // Log the incoming request to delete the patient
        logger.info("Received request to delete patient with ID: {}", id);

        try {
            // Call the service to delete the patient by ID
            patientService.deletePatient(id);

            // On successful deletion, redirect to the patients listing page
            return "redirect:/patients";

        } catch (Exception e) {
            // Log any errors that occur during deletion
            logger.error("Error occurred while deleting patient with ID: {}", id, e);

            // Redirect back to the patient list with an error flag on failure
            return "redirect:/patients?error=true";
        }
    }

}