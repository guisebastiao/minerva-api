package com.minerva.minervaapi.controllers;

import com.minerva.minervaapi.controllers.dtos.DefaultDTO;
import com.minerva.minervaapi.controllers.dtos.UserResetPasswordDTO;
import com.minerva.minervaapi.controllers.dtos.UserUpdateDTO;
import com.minerva.minervaapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<DefaultDTO> findUserById(@PathVariable String userId) {
        DefaultDTO response = userService.findUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping
    public ResponseEntity<DefaultDTO> updateAccount(@RequestBody UserUpdateDTO userUpdateDTO) {
        DefaultDTO response = userService.updateAccount(userUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<DefaultDTO> resetPasswordAccount(@RequestBody UserResetPasswordDTO userResetPasswordDTO) {
        DefaultDTO response = userService.resetPasswordAccount(userResetPasswordDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping
    public ResponseEntity<DefaultDTO> deleteAccount() {
        DefaultDTO response = userService.deleteAccount();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
