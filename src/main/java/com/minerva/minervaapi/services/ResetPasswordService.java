package com.minerva.minervaapi.services;

import com.minerva.minervaapi.controllers.dtos.CreateResetPasswordDTO;
import com.minerva.minervaapi.controllers.dtos.DefaultDTO;
import com.minerva.minervaapi.controllers.dtos.ResetPasswordDTO;

public interface ResetPasswordService {

    DefaultDTO createResetPassword(CreateResetPasswordDTO createResetPasswordDTO);
    DefaultDTO resetPassword(String token, ResetPasswordDTO resetPasswordDTO);
}
