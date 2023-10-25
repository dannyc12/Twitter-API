package com.cooksys.assessment1team3.controllers;


import com.cooksys.assessment1team3.dtos.*;

import com.cooksys.assessment1team3.dtos.CredentialsDto;
import com.cooksys.assessment1team3.dtos.TweetResponseDto;
import com.cooksys.assessment1team3.dtos.UserRequestDto;
import com.cooksys.assessment1team3.dtos.UserResponseDto;

import com.cooksys.assessment1team3.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }

    @GetMapping("/@{username}")
    public UserResponseDto getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/@{username}/following")
    public List<UserResponseDto> getUsersFollowing(@PathVariable String username) {
        return userService.getUsersFollowing(username);
    }

    @GetMapping("/@{username}/followers")
    public List<UserResponseDto> getUserFollowers(@PathVariable String username) {
        return userService.getUserFollowers(username);
    }

    @PatchMapping("/@{username}")
    public UserResponseDto updateUserProfile(@PathVariable String username,
                                             @RequestBody UserRequestDto userRequestDto) {
        return userService.updateUserProfile(username, userRequestDto);
    }



    @GetMapping("/@{username}/tweets")
    public List<TweetResponseDto> getTweetsByUsername(@PathVariable String username) {
        return userService.getTweetsByUsername(username);
    }
  
    @DeleteMapping("/@{username}")
    public UserResponseDto deleteUserByUsername(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
        return userService.deleteUserByUsername(username, credentialsDto);
    }

}
