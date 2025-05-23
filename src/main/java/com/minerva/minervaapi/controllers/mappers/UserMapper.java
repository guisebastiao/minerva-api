package com.minerva.minervaapi.controllers.mappers;

import com.minerva.minervaapi.controllers.dtos.RegisterDTO;
import com.minerva.minervaapi.controllers.dtos.UserResponseDTO;
import com.minerva.minervaapi.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "id", target = "userId")

    UserResponseDTO toDTO(User user);
    User toUser(UserResponseDTO userResponseDTO);
    User toUser(RegisterDTO registerDTO);
}
