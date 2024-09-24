package com.medilabo.risklevel.service;

import com.medilabo.note.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RiskAnalysis {

    public int searchForSymptoms(List<Note> noteList);
}
