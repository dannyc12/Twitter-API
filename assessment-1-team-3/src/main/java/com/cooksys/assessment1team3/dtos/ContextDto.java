package com.cooksys.assessment1team3.dtos;

import com.cooksys.assessment1team3.entities.Tweet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContextDto {

    private Tweet target;

    private List<Tweet> before;

    private List<Tweet> after;
}
