package com.cooksys.assessment1team3.dtos;

import com.cooksys.assessment1team3.entities.Credentials;
import com.cooksys.assessment1team3.entities.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRequestDto {

    private Profile profile;

    private Credentials credentials;

}
