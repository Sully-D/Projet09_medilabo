package com.medilabo.frontend.controller;

import com.medilabo.backend.model.Patient;
import com.medilabo.frontend.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String addPatient(@ModelAttribute Patient patient) {
        logger.info("Received request to add a new patient: {}", patient);

        try {
            patientService.createPatient(patient);
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
            Patient patient = patientService.getPatientById(id);
            model.addAttribute("patient", patient);
            return "edit-patient";

        } catch (Exception e) {
            logger.error("Error occurred while fetching patient with ID: {}", id, e);
            model.addAttribute("error", "An error occurred while fetching the patient");
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