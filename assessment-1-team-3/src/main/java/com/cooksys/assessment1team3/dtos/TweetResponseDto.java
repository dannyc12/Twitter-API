package com.cooksys.assessment1team3.dtos;

import com.cooksys.assessment1team3.entities.Tweet;
import com.cooksys.assessment1team3.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TweetResponseDto {

    private Long id;

    private User author;

    private Timestamp posted;

    private String content;

    private Tweet inReplyTo;

    private Tweet repostOf;
}
