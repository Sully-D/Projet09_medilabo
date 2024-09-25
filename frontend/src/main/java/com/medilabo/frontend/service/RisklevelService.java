package com.medilabo.frontend.service;

import org.springframework.stereotype.Service;


@Service
public interface RisklevelService {

    String getRiskLevelForPatient(String patientId);
}
