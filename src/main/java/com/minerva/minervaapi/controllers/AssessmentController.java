package com.minerva.minervaapi.controllers;

import com.minerva.minervaapi.controllers.dtos.AssessmentDTO;
import com.minerva.minervaapi.controllers.dtos.DefaultDTO;
import com.minerva.minervaapi.services.AssessmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assessments")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    @PostMapping("/{deckId}")
    public ResponseEntity<DefaultDTO> createAssessment(@PathVariable String deckId, @RequestBody @Valid AssessmentDTO assessmentDTO) {
        DefaultDTO response = this.assessmentService.createAssessment(deckId, assessmentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{deckId}")
    public ResponseEntity<DefaultDTO> deleteAssessment(@PathVariable String deckId) {
        DefaultDTO response = this.assessmentService.deleteAssessment(deckId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
