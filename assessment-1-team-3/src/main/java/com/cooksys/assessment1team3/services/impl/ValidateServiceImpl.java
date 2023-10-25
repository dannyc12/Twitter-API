package com.cooksys.assessment1team3.services.impl;

import com.cooksys.assessment1team3.repositories.UserRepository;
import com.cooksys.assessment1team3.services.ValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {

    private final UserRepository userRepository;
    @Override
    public boolean isUsernameAvailable(String username) {
        return (userRepository.findByCredentialsUsername(username) == null);
    }

    @Override
    public boolean usernameExists(String username) {
       return (userRepository.findByCredentialsUsername(username) != null);
    }
}
