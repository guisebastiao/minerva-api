package com.minerva.minervaapi.controllers;

import com.minerva.minervaapi.controllers.dtos.DefaultDTO;
import com.minerva.minervaapi.controllers.dtos.LoginDTO;
import com.minerva.minervaapi.controllers.dtos.RegisterDTO;
import com.minerva.minervaapi.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<DefaultDTO> register(@RequestBody @Valid RegisterDTO registerDTO) {
        DefaultDTO response = this.authService.register(registerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<DefaultDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        DefaultDTO response = this.authService.login(loginDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
