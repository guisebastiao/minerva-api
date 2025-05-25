package com.minerva.minervaapi.services;

import com.minerva.minervaapi.controllers.dtos.AssessmentDTO;
import com.minerva.minervaapi.controllers.dtos.DefaultDTO;

public interface AssessmentService {

    DefaultDTO createAssessment(String deckId, AssessmentDTO assessmentDTO);
    DefaultDTO deleteAssessment(String deckId);
}
