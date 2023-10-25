package com.cooksys.assessment1team3.controllers;

import com.cooksys.assessment1team3.dtos.CredentialsDto;
import com.cooksys.assessment1team3.dtos.TweetResponseDto;
import com.cooksys.assessment1team3.services.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {
    private final TweetService tweetService;

    @GetMapping("/{id}/replies")
    public List<TweetResponseDto> getTweetRepliesById(@PathVariable Long id) {
        return tweetService.getTweetRepliesById(id);
    }

    @DeleteMapping("/{id}")
    public TweetResponseDto deleteTweetById(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
        return tweetService.deleteTweetById(id, credentialsDto);
    }

    @GetMapping("/{id}")
    public TweetResponseDto getTweetById(@PathVariable Long id) {
        return tweetService.getTweetById(id);
    }

}
