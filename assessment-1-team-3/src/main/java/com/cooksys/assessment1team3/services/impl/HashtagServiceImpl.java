package com.cooksys.assessment1team3.services.impl;

import com.cooksys.assessment1team3.dtos.TweetResponseDto;
import com.cooksys.assessment1team3.entities.Hashtag;
import com.cooksys.assessment1team3.entities.Tweet;
import com.cooksys.assessment1team3.exceptions.NotFoundException;
import com.cooksys.assessment1team3.mappers.HashtagMapper;
import com.cooksys.assessment1team3.mappers.TweetMapper;
import com.cooksys.assessment1team3.repositories.HashtagRepository;
import com.cooksys.assessment1team3.services.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;
    private final TweetMapper tweetMapper;

    @Override
    public List<TweetResponseDto> getTagsByLabel(String label) {
        Hashtag hashtags = hashtagRepository.findByLabel(label);
        if (hashtags == null) {
            throw new NotFoundException("No Hashtags found with tags: " + label);
        }
        List<Tweet> tweets = hashtags.getTweets();
        List<TweetResponseDto> tweetResponseDtos = tweetMapper.entitiesToDtos(tweets);
        for (int i = 0; i < tweetResponseDtos.size() - 1; i++) {
            for (int j = i + 1; j < tweetResponseDtos.size(); j++) {
                TweetResponseDto tweet1 = tweetResponseDtos.get(i);
                TweetResponseDto tweet2 = tweetResponseDtos.get(j);
                if (tweet2.getPosted().compareTo(tweet1.getPosted()) > 0) {
                    // Swap tweet1 and tweet2 if tweet2 is newer than tweet1
                    TweetResponseDto temp = tweet1;
                    tweetResponseDtos.set(i, tweet2);
                    tweetResponseDtos.set(j, temp);
                }
            }
        }
        return tweetResponseDtos;
    }
}
