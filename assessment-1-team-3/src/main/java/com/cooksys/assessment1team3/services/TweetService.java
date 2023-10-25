package com.cooksys.assessment1team3.services;

import com.cooksys.assessment1team3.dtos.TweetResponseDto;
import com.cooksys.assessment1team3.dtos.UserResponseDto;

import java.util.List;

public interface TweetService {
    List<TweetResponseDto> getTweetRepliesById(Long id);

    List<UserResponseDto> getTweetLikes(Long id);
}
