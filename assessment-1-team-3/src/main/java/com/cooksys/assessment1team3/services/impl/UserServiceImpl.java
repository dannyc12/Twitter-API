package com.cooksys.assessment1team3.services.impl;

import com.cooksys.assessment1team3.dtos.*;
import com.cooksys.assessment1team3.entities.Credentials;
import com.cooksys.assessment1team3.entities.User;
import com.cooksys.assessment1team3.exceptions.BadRequestException;
import com.cooksys.assessment1team3.exceptions.NotAuthorizedException;
import com.cooksys.assessment1team3.exceptions.NotFoundException;
import com.cooksys.assessment1team3.mappers.CredentialsMapper;
import com.cooksys.assessment1team3.mappers.ProfileMapper;
import com.cooksys.assessment1team3.mappers.TweetMapper;
import com.cooksys.assessment1team3.mappers.UserMapper;
import com.cooksys.assessment1team3.repositories.TweetRepository;
import com.cooksys.assessment1team3.repositories.UserRepository;
import com.cooksys.assessment1team3.services.UserService;
import com.cooksys.assessment1team3.utils.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final Utility utility;
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    private final TweetMapper tweetMapper;
    private final UserMapper userMapper;
    private final ProfileMapper profileMapper;
    private final CredentialsMapper credentialsMapper;

    @Override
    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findByCredentialsUsername(username);
        utility.validateUserExists(user, username);
        return userMapper.entityToDto(user);
    }

    @Override
    public List<TweetResponseDto> getTweetsByUsername(String username) {
        User user = userRepository.findByCredentialsUsername(username);
        utility.validateUserExists(user, username);
        return tweetMapper.entitiesToDtos(tweetRepository.findAllTweetsByAuthorAndDeletedIsFalseOrderByPostedDesc(user));
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userMapper.entitiesToDtos(userRepository.findAllByDeletedFalse());
    }

    @Override
    public UserResponseDto deleteUserByUsername(String username, CredentialsDto credentialsDto) {
        User user = userRepository.findByCredentialsUsername(username);
        utility.validateUserExists(user, username);
        Credentials credentials = credentialsMapper.requestToEntity(credentialsDto);
        utility.validateCredentials(user, credentials);
        user.setDeleted(true);
        return userMapper.entityToDto(userRepository.saveAndFlush(user));
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        String username = userRequestDto.getCredentials().getUsername();
        utility.validateUserRequest(userRequestDto);
        // leaving this because it's a special case
        User user = userRepository.findByCredentialsUsername(username);
        if (user != null) {
            if (user.isDeleted()) {
                user.setDeleted(false);
                userRepository.saveAndFlush(user);
            } else {
                throw new BadRequestException("User already exists.");
            }
        } else {
            user = userRepository.saveAndFlush(userMapper.requestToEntity(userRequestDto));
        }
        return userMapper.entityToDto(user);
    }

    @Override
    public List<UserResponseDto> getUsersFollowing(String username) {
        User user = userRepository.findByCredentialsUsername(username);
        if (user == null || user.isDeleted()) {
            throw new NotFoundException("No user found with username: " + username);
        }
        return userMapper.entitiesToDtos(userRepository.findAllByFollowersAndDeletedFalse(user));
    }

    @Override
    public List<UserResponseDto> getUserFollowers(String username) {
        User user = userRepository.findByCredentialsUsername(username);
        if (user == null || user.isDeleted()) {
            throw new NotFoundException("No user found with username: " + username);
        }
        return userMapper.entitiesToDtos(userRepository.findAllByFollowingAndDeletedFalse(user));
    }

    public UserResponseDto updateUserProfile(String username, UserRequestDto userRequestDto) {
        User user = userRepository.findByCredentialsUsername(username);
        utility.validateUserExists(user, username);
        Credentials credentials = credentialsMapper.requestToEntity(userRequestDto.getCredentials());
        ProfileDto profileDto = userRequestDto.getProfile();
        utility.validateCredentials(user, credentials);
        utility.validateUserEmail(profileDto.getEmail());
        user.setProfile(profileMapper.requestToEntity(profileDto));
        return userMapper.entityToDto(userRepository.saveAndFlush(user));
    }

    public void postUsernameToFollow(String username, CredentialsDto credentialsDto) {
        User userToFollow = userRepository.findByCredentialsUsername(username);
        utility.validateUserExists(userToFollow, username);

        User newFollower = userRepository.findByCredentialsUsername(credentialsDto.getUsername());
        String newUserName = credentialsDto.getUsername();
        Credentials credentials = credentialsMapper.requestToEntity(credentialsDto);
        utility.validateCredentials(newFollower, credentials);
        utility.validateUserExists(newFollower, newUserName);

        utility.validateUserFollower(userToFollow, newFollower);

        userToFollow.getFollowers().add(newFollower);
        userRepository.saveAndFlush(userToFollow);
    }

    @Override
    public void postUsernameToUnfollow(String username, CredentialsDto credentialsDto) {
        User userToFollow = userRepository.findByCredentialsUsername(username);
        utility.validateUserExists(userToFollow, username);

        User newFollower = userRepository.findByCredentialsUsername(credentialsDto.getUsername());
        String newUserName = credentialsDto.getUsername();
        Credentials credentials = credentialsMapper.requestToEntity(credentialsDto);
        utility.validateCredentials(newFollower, credentials);
        utility.validateUserExists(newFollower, newUserName);

        if(!userToFollow.getFollowers().contains(newFollower)){
            throw new NotAuthorizedException("You are Not following "+newUserName+ ".");
        }

        userToFollow.getFollowers().remove(newFollower);
        userRepository.saveAndFlush(userToFollow);
    }
}
