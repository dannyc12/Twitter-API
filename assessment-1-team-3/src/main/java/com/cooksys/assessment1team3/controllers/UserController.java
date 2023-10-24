package com.cooksys.assessment1team3.controllers;

import com.cooksys.assessment1team3.dtos.UserResponseDto;
import com.cooksys.assessment1team3.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{username}")
    public UserResponseDto getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }
}
