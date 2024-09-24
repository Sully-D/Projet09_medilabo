package com.medilabo.risklevel.service.impl;

import com.medilabo.note.model.Note;
import com.medilabo.risklevel.service.RiskAnalysis;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;


@Service
public class RiskAnalysisImpl implements RiskAnalysis {

    @Override
    public int searchForSymptoms(List<Note> noteList) {

        HashSet<String> symptoms = new HashSet<>();
        symptoms.add("hémoglobine A1C");
        symptoms.add("microalbumine");
        symptoms.add("taille");
        symptoms.add("poids");
        symptoms.add("fumeur");
        symptoms.add("fumeuse");
        symptoms.add("anormal");
        symptoms.add("cholestérol");
        symptoms.add("vertiges");
        symptoms.add("rechute");
        symptoms.add("réaction");
        symptoms.add("anticorps");

        int countSysmptoms = 0;

        for (Note note : noteList) {
            String content = note.getNoteContent();
            if (content != null) {
                for (String symptom : symptoms) {
                    if (content.toLowerCase().contains(symptom.toLowerCase())) {
                        countSysmptoms++;
                        break;
                    }
                }
            }
        }

        return countSysmptoms;
    }
}
