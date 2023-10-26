package com.cooksys.assessment1team3.services.impl;

import com.cooksys.assessment1team3.dtos.CredentialsDto;
import com.cooksys.assessment1team3.dtos.TweetResponseDto;
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
    private final UserRepository userRepository;
    private final Utility utility;
    private final CredentialsMapper credentialsMapper;


    @Override
    public List<TweetResponseDto> getAllTweets() {
        return tweetMapper.entitiesToDtos(tweetRepository.findAllByDeletedFalseOrderByPostedDesc());
    }

    @Override
    public List<TweetResponseDto> getTweetRepliesById(Long id) {
        Optional<Tweet> optionalTweet = tweetRepository.findById(id);
        if (optionalTweet.isEmpty() || optionalTweet.get().isDeleted()) {
            throw new NotFoundException("No tweet found with id: " + id);
        }
        return tweetMapper.entitiesToDtos(tweetRepository.findAllRepliesToTweet(id));
    }

    @Override
    public TweetResponseDto repostTweet(Long id, CredentialsDto credentialsDto) {
        Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);
        utility.validateTweetExists(tweet, id);
        User user = userRepository.findByCredentialsUsername(credentialsDto.getUsername());
        utility.validateUserExists(user, credentialsDto.getUsername());
        utility.validateCredentials(user, credentialsMapper.requestToEntity(credentialsDto));
        Tweet repost = tweet;
        repost.setRepostOf(tweet);
        repost.setContent(null);
        System.out.println("Trying to set user...");
        repost.setAuthor(user);
        System.out.println("Successfully set user.");
        tweetRepository.saveAndFlush(repost);
        System.out.println("Successfully saved repost");
        return null;
    }
    @Override
    public TweetResponseDto deleteTweetById(Long id, CredentialsDto credentialsDto) {
        Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);
        utility.validateTweetExists(tweet, id);
        User user = userRepository.findByCredentialsUsername(credentialsDto.getUsername());
        utility.validateCredentials(user, credentialsMapper.requestToEntity(credentialsDto));
        tweet.setDeleted(true);
        tweetRepository.saveAndFlush(tweet);
        return tweetMapper.entityToDto(tweet);
    }

    @Override
    public void likeTweetById(Long id, CredentialsDto credentialsDto) {
        Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);
        utility.validateTweetExists(tweet, id);
        User user = userRepository.findByCredentialsUsername(credentialsDto.getUsername());
        utility.validateUserExists(user, credentialsDto.getUsername());
        utility.validateCredentials(user, credentialsMapper.requestToEntity(credentialsDto));
        List<Tweet> likedTweets = user.getUserLikes();
        if (!likedTweets.contains(tweet)) {
            likedTweets.add(tweet);
            user.setUserLikes(likedTweets);
            userRepository.saveAndFlush(user);
        }
    }

    @Override
    public TweetResponseDto getTweetById(Long id) {
        Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);
        utility.validateTweetExists(tweet, id);
        return tweetMapper.entityToDto(tweetRepository.findByIdAndDeletedFalse(id));
    }
}
