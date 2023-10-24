package com.cooksys.assessment1team3.services;

import com.cooksys.assessment1team3.dtos.TweetResponseDto;
import com.cooksys.assessment1team3.dtos.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto getUserByUsername(String username);

    List<TweetResponseDto> getTweetsByUsername(String username);

    List<UserResponseDto> getAllUsers();

}
