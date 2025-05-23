package com.minerva.minervaapi.controllers;

import com.minerva.minervaapi.controllers.dtos.CreateResetPasswordDTO;
import com.minerva.minervaapi.controllers.dtos.DefaultDTO;
import com.minerva.minervaapi.controllers.dtos.ResetPasswordDTO;
import com.minerva.minervaapi.services.ResetPasswordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reset-password")
public class ResetPasswordController {

    @Autowired
    private ResetPasswordService resetPasswordService;

    @PostMapping
    public ResponseEntity<DefaultDTO> createResetPassword(@RequestBody @Valid CreateResetPasswordDTO createResetPasswordDTO) {
        DefaultDTO response = resetPasswordService.createResetPassword(createResetPasswordDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping
    public ResponseEntity<DefaultDTO> resetPassword(@RequestParam String token, @RequestBody @Valid ResetPasswordDTO resetPasswordDTO) {
        DefaultDTO response = resetPasswordService.resetPassword(token, resetPasswordDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
