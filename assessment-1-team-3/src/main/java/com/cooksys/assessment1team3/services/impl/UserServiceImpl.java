package com.cooksys.assessment1team3.services.impl;

import com.cooksys.assessment1team3.dtos.TweetResponseDto;
import com.cooksys.assessment1team3.dtos.CredentialsDto;
import com.cooksys.assessment1team3.dtos.UserRequestDto;
import com.cooksys.assessment1team3.dtos.UserResponseDto;
import com.cooksys.assessment1team3.entities.Tweet;
import com.cooksys.assessment1team3.entities.User;
import com.cooksys.assessment1team3.exceptions.BadRequestException;
import com.cooksys.assessment1team3.exceptions.NotAuthorizedException;
import com.cooksys.assessment1team3.exceptions.NotFoundException;
import com.cooksys.assessment1team3.mappers.TweetMapper;
import com.cooksys.assessment1team3.mappers.UserMapper;
import com.cooksys.assessment1team3.repositories.TweetRepository;
import com.cooksys.assessment1team3.repositories.UserRepository;
import com.cooksys.assessment1team3.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    private final UserMapper userMapper;
    private final TweetMapper tweetMapper;

    @Override
    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findByCredentialsUsername(username);
        if (user == null) {
            throw new NotFoundException("No user found with username: " + username);
        }
        return userMapper.entityToDto(user);
    }

    @Override
    public List<TweetResponseDto> getTweetsByUsername(String username) {
        User user = userRepository.findByCredentialsUsername(username);
        if (user == null || user.isDeleted()) {
            throw new NotFoundException("No user found with username: " + username);
        }
        return tweetMapper.entitiesToDtos(tweetRepository.findAllTweetsByAuthorAndDeletedIsFalseOrderByPostedDesc(user));
    }

    public List<UserResponseDto> getAllUsers() {
        return userMapper.entitiesToDtos(userRepository.findAllByDeletedFalse());
    }

    @Override
    public UserResponseDto deleteUserByUsername(String username, CredentialsDto credentialsDto) {
        User user = userRepository.findByCredentialsUsername(username);
        if (user == null || user.isDeleted()) {
            throw new NotFoundException("No user found with username: " + username);
        }
        if (!user.getCredentials().getUsername().equals(credentialsDto.getUsername())
                || !user.getCredentials().getPassword().equals(credentialsDto.getPassword())) {
            throw new NotAuthorizedException("You are not authorized to delete this user.");
        }
        user.setDeleted(true);
        return userMapper.entityToDto(userRepository.saveAndFlush(user));
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        String username = userRequestDto.getCredentials().getUsername();
        if (username == null || username.isBlank()) {
            throw new BadRequestException("Username is required.");
        }
        String password = userRequestDto.getCredentials().getPassword();
        if (password == null || password.isBlank()) {
            throw new BadRequestException("Password is required.");
        }
        String email = userRequestDto.getProfile().getEmail();
        if (email == null || email.isBlank()) {
            throw new BadRequestException("Email is required.");
        }
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
}
