package com.cooksys.assessment1team3.dtos;

import com.cooksys.assessment1team3.entities.Tweet;
import com.cooksys.assessment1team3.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TweetRequestDto {

    private User author;

    private String content;

    private Tweet inReplyTo;

    private Tweet repostOf;

}
