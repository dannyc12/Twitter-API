package com.cooksys.assessment1team3.services.impl;

import com.cooksys.assessment1team3.dtos.CredentialsDto;
import com.cooksys.assessment1team3.dtos.UserResponseDto;
import com.cooksys.assessment1team3.entities.User;
import com.cooksys.assessment1team3.exceptions.NotAuthorizedException;
import com.cooksys.assessment1team3.exceptions.NotFoundException;
import com.cooksys.assessment1team3.mappers.UserMapper;
import com.cooksys.assessment1team3.repositories.UserRepository;
import com.cooksys.assessment1team3.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findByCredentialsUsername(username);
        if (user == null) {
            throw new NotFoundException("No user found with username: " + username);
        }
        return userMapper.entityToDto(user);
    }

    @Override
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
}
