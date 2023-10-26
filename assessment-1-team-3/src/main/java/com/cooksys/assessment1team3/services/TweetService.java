package com.cooksys.assessment1team3.services;

import com.cooksys.assessment1team3.dtos.CredentialsDto;
import com.cooksys.assessment1team3.dtos.TweetResponseDto;

import java.util.List;

public interface TweetService {
    List<TweetResponseDto> getAllTweets();

    List<TweetResponseDto> getTweetRepliesById(Long id);

    TweetResponseDto repostTweet(Long id, CredentialsDto credentialsDto);
}
