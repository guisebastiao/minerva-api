package com.minerva.minervaapi.services;

import com.minerva.minervaapi.controllers.dtos.DefaultDTO;
import com.minerva.minervaapi.controllers.dtos.UserResetPasswordDTO;
import com.minerva.minervaapi.controllers.dtos.UserUpdateDTO;

public interface UserService {

    DefaultDTO findUserById(String userId);
    DefaultDTO resetPasswordAccount(UserResetPasswordDTO userResetPasswordDTO);
    DefaultDTO updateAccount(UserUpdateDTO userUpdateDTO);
    DefaultDTO deleteAccount();
}
