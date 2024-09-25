package com.medilabo.risklevel.service.impl;

import com.medilabo.note.model.Note;
import com.medilabo.risklevel.service.RiskAnalysis;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;


@Service
public class RiskAnalysisImpl implements RiskAnalysis {

    private static final Set<String> SYMPTOMS = Set.of(
            "hémoglobine A1C", "microalbumine", "taille", "poids", "fumeur",
            "fumeuse", "anormal", "cholestérol", "vertiges", "rechute",
            "réaction", "anticorps"
    );

    @Override
    public int searchForSymptoms(List<Note> noteList) {
        int countSymptoms = 0;

        Set<Pattern> symptomPatterns = new HashSet<>();
        for (String symptom : SYMPTOMS) {
            symptomPatterns.add(Pattern.compile(Pattern.quote(symptom), Pattern.CASE_INSENSITIVE));
        }

        for (Note note : noteList) {
            String content = note.getNoteContent();
            if (content != null) {
                String lowerCaseNoteContent = content.toLowerCase();
                for (Pattern symptom : symptomPatterns) {
                    if (symptom.matcher(lowerCaseNoteContent).find()) {
                        countSymptoms++;
                    }
                }
            }
        }

        return countSymptoms;
    }
}
