package com.cooksys.assessment1team3.services.impl;

import com.cooksys.assessment1team3.dtos.CredentialsDto;
import com.cooksys.assessment1team3.dtos.TweetResponseDto;
import com.cooksys.assessment1team3.entities.Credentials;
import com.cooksys.assessment1team3.entities.Tweet;
import com.cooksys.assessment1team3.entities.User;
import com.cooksys.assessment1team3.exceptions.NotAuthorizedException;
import com.cooksys.assessment1team3.exceptions.NotFoundException;
import com.cooksys.assessment1team3.mappers.CredentialsMapper;
import com.cooksys.assessment1team3.mappers.TweetMapper;
import com.cooksys.assessment1team3.repositories.TweetRepository;
import com.cooksys.assessment1team3.repositories.UserRepository;
import com.cooksys.assessment1team3.services.TweetService;
import com.cooksys.assessment1team3.utils.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {
    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;
    private final CredentialsMapper credentialsMapper;
    private final UserRepository userRepository;
    private final Utility utility;

    @Override
    public List<TweetResponseDto> getAllTweets() {
        return tweetMapper.entitiesToDtos(tweetRepository.findAllByDeletedFalseOrderByPostedDesc());
    }

    public List<TweetResponseDto> getTweetRepliesById(Long id) {
        Optional<Tweet> optionalTweet = tweetRepository.findById(id);
        if (optionalTweet.isEmpty() || optionalTweet.get().isDeleted()) {
            throw new NotFoundException("No tweet found with id: " + id);
        }
        return tweetMapper.entitiesToDtos(tweetRepository.findAllRepliesToTweet(id));
    }

    @Override
    public TweetResponseDto repostTweet(Long id, CredentialsDto credentialsDto) {
        Optional<Tweet> optionalTweet = tweetRepository.findById(id);
        if (optionalTweet.isEmpty() || optionalTweet.get().isDeleted()) {
            throw new NotFoundException("No tweet found with id: " + id);
        }
        System.out.println(credentialsDto.getUsername());
        String username = credentialsDto.getUsername();
//        Credentials credentials = credentialsMapper.requestToEntity(credentialsDto);
        // validate user exists
        User author = userRepository.findByCredentialsUsername(username);
        utility.validateUserExists(author, username);
        if (!author.getCredentials().getUsername().equals(credentialsDto.getUsername())
                || !author.getCredentials().getPassword().equals(credentialsDto.getPassword())
        ) {
            throw new NotAuthorizedException("You are not authorized to repost this tweet.");
        }
        Tweet tweet = optionalTweet.get();
        tweet.setRepostOf(optionalTweet.get());
        tweet.setContent(null);
        tweet.setAuthor(author);
        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweet));
    }
}
