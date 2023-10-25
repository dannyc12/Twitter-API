package com.cooksys.assessment1team3.utils;

import com.cooksys.assessment1team3.dtos.UserRequestDto;
import com.cooksys.assessment1team3.entities.Credentials;
import com.cooksys.assessment1team3.entities.User;
import com.cooksys.assessment1team3.exceptions.BadRequestException;
import com.cooksys.assessment1team3.exceptions.NotAuthorizedException;
import com.cooksys.assessment1team3.exceptions.NotFoundException;
import com.cooksys.assessment1team3.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class Utility {
    private final UserRepository userRepository;

    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    public void validateUserEmail(String email) {
        if (email == null) {
            throw new BadRequestException("An email is required to update user profile.");
        }
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new BadRequestException("You must pass a valid email to update the user profile.");
        }
    }

    public User getUserByUsername(String username) {
        User user = userRepository.findByCredentialsUsername(username);
        if (user == null || user.isDeleted()) {
            throw new NotFoundException("No user found with username: " + username);
        }
        return user;
    }

    public void validateCredentials(User user, Credentials credentials) {
        if (!user.getCredentials().getUsername().equals(credentials.getUsername())
                || !user.getCredentials().getPassword().equals(credentials.getPassword())) {
            // we could pass in a specific string here for each case, but we'd need another parameter
            throw new NotAuthorizedException("You are not authorized to perform this action.");
        }
    }

    public void validateUserRequest(UserRequestDto userRequestDto) {
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
    }

}
