package com.medilabo.risklevel.service.impl;

import com.medilabo.risklevel.model.Note;
import com.medilabo.risklevel.service.RiskAnalysis;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * RiskAnalysisImpl class implements the RiskAnalysis interface to provide methods for searching symptoms in notes.
 * Initializes a set of static symptoms and their corresponding patterns for efficient symptom detection.
 * The searchForSymptoms method counts the occurrences of symptoms in the provided list of notes.
 * The countSymptomsInNoteContent method counts the occurrences of symptoms in a single note's content.
 */
@Service
public class RiskAnalysisImpl implements RiskAnalysis {

    // List of static symptoms and compilation of patterns at class initialization
    private static final Set<String> SYMPTOMS = Set.of(
            "hémoglobine A1C", "microalbumine", "taille", "poids", "fumeur",
            "fumeuse", "anormal", "cholestérol", "vertiges", "rechute",
            "réaction", "anticorps"
    );


    // Initializes a set of patterns for symptom matching by streaming the predefined list of symptoms.
    private static final Set<Pattern> SYMPTOM_PATTERNS = SYMPTOMS.stream()
            .map(symptom -> Pattern.compile(Pattern.quote(symptom), Pattern.CASE_INSENSITIVE))
            .collect(Collectors.toSet());


    /**
     * Searches for symptoms in the provided list of notes.
     * If the list is empty or null, returns 0.
     * Counts the occurrences of symptoms in each note's content by calling the 'countSymptomsInNoteContent' method.
     *
     * @param noteList The list of notes to search for symptoms.
     * @return The total count of symptoms found in the notes.
     */
    @Override
    public int searchForSymptoms(List<Note> noteList) {
        if (noteList == null || noteList.isEmpty()) {
            return 0; // Return 0 if the list is null or empty
        }

        return noteList.stream()
                .filter(note -> note.getNoteContent() != null)
                .mapToInt(note -> countSymptomsInNoteContent(note.getNoteContent()))
                .sum();
    }


    /**
     * Counts the number of symptoms in the given note content.
     *
     * @param noteContent the content of the note to analyze for symptoms
     * @return the count of symptoms found in the note content
     */
    private int countSymptomsInNoteContent(String noteContent) {
        int count = 0;
        String lowerCaseNoteContent = noteContent.toLowerCase();
        for (Pattern symptomPattern : SYMPTOM_PATTERNS) {
            if (symptomPattern.matcher(lowerCaseNoteContent).find()) {
                count++;
            }
        }
        return count;
    }
}
