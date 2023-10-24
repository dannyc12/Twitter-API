package com.cooksys.assessment1team3.mappers;

import com.cooksys.assessment1team3.dtos.UserRequestDto;
import com.cooksys.assessment1team3.dtos.UserResponseDto;
import com.cooksys.assessment1team3.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto entityToDto(User user);

    List<UserResponseDto> entitiesToDto(List<User> entities);

    User requestToDto(UserRequestDto userRequestDto);
}
