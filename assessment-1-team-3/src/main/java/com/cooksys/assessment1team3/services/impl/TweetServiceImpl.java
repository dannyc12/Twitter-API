package com.cooksys.assessment1team3.services.impl;

import com.cooksys.assessment1team3.dtos.CredentialsDto;
import com.cooksys.assessment1team3.dtos.TweetRequestDto;
import com.cooksys.assessment1team3.dtos.TweetResponseDto;
import com.cooksys.assessment1team3.entities.Credentials;
import com.cooksys.assessment1team3.entities.Tweet;
import com.cooksys.assessment1team3.entities.User;
import com.cooksys.assessment1team3.exceptions.BadRequestException;
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
    private final Utility utility;
    private final UserRepository userRepository;
    private final CredentialsMapper credentialsMapper;

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
    public TweetResponseDto postReplayTweetWithId(Long id, TweetRequestDto tweetRequestDto) {
        Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);
        utility.validateTweetExists(tweet, id);

        User user = userRepository.findByCredentialsUsername(tweetRequestDto.getCredentials().getUsername());
        List<User> userList = userRepository.findAllByDeletedFalse();
        if (!userList.contains(user))
            throw new NotFoundException("User do not exist or is deleted");
        if(!user.getCredentials().getPassword().equals(tweetRequestDto.getCredentials().getPassword()))
            throw new NotFoundException("Password do not match");
        if (tweetRequestDto.getContent() == null || tweetRequestDto.getContent().isEmpty())
            throw new BadRequestException("There is no Content in the request");

        Tweet replyTweet = new Tweet();
        replyTweet.setAuthor(user);
        replyTweet.setInReplyTo(tweet);
        replyTweet.setContent(tweetRequestDto.getContent());

        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(replyTweet));
    }

    @Override
    public TweetResponseDto getTweetById(Long id) {
        Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);
        utility.validateTweetExists(tweet, id);
        return tweetMapper.entityToDto(tweetRepository.findByIdAndDeletedFalse(id));
    }
}
