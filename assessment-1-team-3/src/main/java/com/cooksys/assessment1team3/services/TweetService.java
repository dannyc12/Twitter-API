package com.cooksys.assessment1team3.services;

import com.cooksys.assessment1team3.dtos.ContextDto;
import com.cooksys.assessment1team3.dtos.CredentialsDto;
import com.cooksys.assessment1team3.dtos.HashtagDto;
import com.cooksys.assessment1team3.dtos.TweetResponseDto;
import com.cooksys.assessment1team3.dtos.UserResponseDto;

import java.util.List;

public interface TweetService {
    List<TweetResponseDto> getAllTweets();

    List<TweetResponseDto> getTweetRepliesById(Long id);

    ContextDto getTweetContext(Long id);

    List<UserResponseDto> getTweetLikes(Long id);

    TweetResponseDto getTweetById(Long id);

    TweetResponseDto deleteTweetById(Long id, CredentialsDto credentialsDto);
    List<HashtagDto> getHashtagsByTweet(Long id);

    void likeTweetById(Long id, CredentialsDto credentialsDto);

}
