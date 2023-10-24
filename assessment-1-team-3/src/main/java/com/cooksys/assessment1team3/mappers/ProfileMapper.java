package com.cooksys.assessment1team3.mappers;

import com.cooksys.assessment1team3.dtos.ProfileDto;
import com.cooksys.assessment1team3.entities.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    Profile requestToEntity(ProfileDto profileDto);

    ProfileDto entityToDto(Profile entity);


}
