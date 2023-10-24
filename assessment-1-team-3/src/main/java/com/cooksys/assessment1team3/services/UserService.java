package com.cooksys.assessment1team3.services;

import com.cooksys.assessment1team3.dtos.UserResponseDto;

public interface UserService {
    UserResponseDto getUserByUsername(String username);
}
