package com.cooksys.assessment1team3.services.impl;

import com.cooksys.assessment1team3.dtos.CredentialsDto;
import com.cooksys.assessment1team3.dtos.ProfileDto;
import com.cooksys.assessment1team3.dtos.UserRequestDto;
import com.cooksys.assessment1team3.dtos.UserResponseDto;
import com.cooksys.assessment1team3.entities.User;
import com.cooksys.assessment1team3.exceptions.BadRequestException;
import com.cooksys.assessment1team3.exceptions.NotAuthorizedException;
import com.cooksys.assessment1team3.exceptions.NotFoundException;
import com.cooksys.assessment1team3.mappers.ProfileMapper;
import com.cooksys.assessment1team3.mappers.UserMapper;
import com.cooksys.assessment1team3.repositories.UserRepository;
import com.cooksys.assessment1team3.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProfileMapper profileMapper;

    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    private boolean validateUserEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findByCredentialsUsername(username);
        if (user == null || user.isDeleted()) {
            throw new NotFoundException("No user found with username: " + username);
        }
        return userMapper.entityToDto(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userMapper.entitiesToDtos(userRepository.findAllByDeletedFalse());
    }

    @Override
    public UserResponseDto updateUserProfile(String username, UserRequestDto userRequestDto) {
        // need to extract this portion as a helper method 'getUserByUsername(username)' later
        User user = userRepository.findByCredentialsUsername(username);
        if (user == null || user.isDeleted()) {
            throw new NotFoundException("No user found with username: " + username);
        }
        CredentialsDto credentialsDto = userRequestDto.getCredentials();
        ProfileDto profileDto = userRequestDto.getProfile();
        // need to extract this portion as a helper method 'validateUserCredentials(user, credentialsDto)' later
        if (!user.getCredentials().getUsername().equals(credentialsDto.getUsername())
                || !user.getCredentials().getPassword().equals(credentialsDto.getPassword())) {
            throw new NotAuthorizedException("You are not authorized to update this user.");
        }
        if (profileDto.getEmail() == null) {
            throw new BadRequestException("An email is required to update user profile.");
        }
        if (!validateUserEmail(profileDto.getEmail())) {
            throw new BadRequestException("You must pass a valid email to update the user profile.");
        }
        user.setProfile(profileMapper.requestToEntity(profileDto));
        return userMapper.entityToDto(userRepository.saveAndFlush(user));
    }
}
