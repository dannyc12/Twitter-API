package com.cooksys.assessment1team3.services.impl;

import com.cooksys.assessment1team3.dtos.*;
import com.cooksys.assessment1team3.entities.Tweet;
import com.cooksys.assessment1team3.entities.User;
import com.cooksys.assessment1team3.mappers.CredentialsMapper;
import com.cooksys.assessment1team3.mappers.HashtagMapper;
import com.cooksys.assessment1team3.mappers.TweetMapper;
import com.cooksys.assessment1team3.mappers.UserMapper;
import com.cooksys.assessment1team3.repositories.TweetRepository;
import com.cooksys.assessment1team3.repositories.UserRepository;
import com.cooksys.assessment1team3.services.TweetService;
import com.cooksys.assessment1team3.utils.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {
    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;
    private final UserMapper userMapper;
    
    private final UserRepository userRepository;
    private final Utility utility;
    private final CredentialsMapper credentialsMapper;
    private final HashtagMapper hashtagMapper;

    @Override
    public List<TweetResponseDto> getAllTweets() {
        return tweetMapper.entitiesToDtos(tweetRepository.findAllByDeletedFalseOrderByPostedDesc());
    }

    @Override
    public List<TweetResponseDto> getTweetRepliesById(Long id) {
        Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);
        utility.validateTweetExists(tweet, id);
        return tweetMapper.entitiesToDtos(tweetRepository.findAllRepliesToTweet(id));
    }

    @Override
    public ContextDto getTweetContext(Long id) {
        Tweet targetTweet = tweetRepository.findByIdAndDeletedFalse(id);
        utility.validateTweetExists(targetTweet, id);
        // Helena says we're supposed to create new ContextDto instance because we don't have an entity for it
        ContextDto contextDto = new ContextDto();
        contextDto.setBefore(new ArrayList<>());
        List<Tweet> beforeTweets = new ArrayList<>();
        contextDto.setTarget(tweetMapper.entityToDto(targetTweet));
        // loop through each tweet build a chain of inReplyTo
        Tweet tempTweet = targetTweet;
        while (tempTweet.getInReplyTo() != null) {
            Tweet preceedingTweet = tempTweet.getInReplyTo();
//            contextDto.getBefore().add(tweetMapper.entityToDto(preceedingTweet));
//            tempTweet = tweetRepository.findByIdAndDeletedFalse(preceedingTweet.getId());
//            utility.validateTweetExists(tempTweet, id);
            beforeTweets.add(preceedingTweet);
            tempTweet = preceedingTweet;
        }
        // remove tweets that have been deleted
        beforeTweets.removeIf(Tweet::isDeleted);
        // reverse Context.before to set in chronological order
        Collections.reverse(beforeTweets);
        contextDto.setBefore(tweetMapper.entitiesToDtos(beforeTweets));

        List<Tweet> repliesToTarget = targetTweet.getReplies();
        int index = 0;
        // replies.size() changes as we add, so the list will keep growing until there are no replies left to add
        while (index < repliesToTarget.size()) {
            Tweet currentTweet = repliesToTarget.get(index);
            List<Tweet> currentReplies = currentTweet.getReplies();
            if (currentReplies != null && !currentReplies.isEmpty()) {
                repliesToTarget.addAll(currentReplies);
            }
            index++;
        }
        // remove any replies that have been 'deleted'
        repliesToTarget.removeIf(Tweet::isDeleted);
        // sort the repliesToTarget by posted
        Collections.sort(repliesToTarget, new Comparator<Tweet>() {
            @Override
            public int compare(Tweet tweet1, Tweet tweet2) {
                return tweet1.getPosted().compareTo(tweet2.getPosted());
            }
        });
        contextDto.setAfter(tweetMapper.entitiesToDtos(repliesToTarget));
        return contextDto;
    }

    public TweetResponseDto repostTweet(Long id, CredentialsDto credentialsDto) {
        Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);
        utility.validateTweetExists(tweet, id);
        User user = userRepository.findByCredentialsUsername(credentialsDto.getUsername());
        utility.validateUserExists(user, credentialsDto.getUsername());
        utility.validateCredentials(user, credentialsMapper.requestToEntity(credentialsDto));
        Tweet repost = new Tweet();
        repost.setRepostOf(tweet);
        repost.setContent(null);
        repost.setAuthor(user);
        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(repost));
    }

    @Override
    public List<UserResponseDto> getTweetLikes(Long id) {
        Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);
        utility.validateTweetExists(tweet, id);
        return userMapper.entitiesToDtos(tweetRepository.findAllUserLikes(id));
    }

    @Override
    public List<UserResponseDto> getTweetMentions(Long id) {
        Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);
        utility.validateTweetExists(tweet, id);
        return userMapper.entitiesToDtos(tweetRepository.findAllUserMentions(id));
    }

    @Override
    public TweetResponseDto deleteTweetById(Long id, CredentialsDto credentialsDto) {
        Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);
        utility.validateTweetExists(tweet, id);
        User user = userRepository.findByCredentialsUsername(credentialsDto.getUsername());
        utility.validateUserExists(user, credentialsDto.getUsername());
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

    @Override
    public List<HashtagDto> getHashtagsByTweet(Long id) {
        Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);
        utility.validateTweetExists(tweet, id);
        return hashtagMapper.entitiesToDtos(tweet.getHashtags());
    }
}
