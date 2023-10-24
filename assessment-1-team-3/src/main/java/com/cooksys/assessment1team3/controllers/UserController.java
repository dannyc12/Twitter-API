package com.cooksys.assessment1team3.controllers;

import com.cooksys.assessment1team3.dtos.TweetResponseDto;
import com.cooksys.assessment1team3.dtos.UserResponseDto;
import com.cooksys.assessment1team3.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/@{username}")
    public UserResponseDto getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/@{username}/tweets")
    public List<TweetResponseDto> getTweetsByUsername(@PathVariable String username) {
        return userService.getTweetsByUsername(username);
    }
}
