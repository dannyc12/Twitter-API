package com.cooksys.assessment1team3.entities;

import jakarta.persistence.Embeddable;

import java.sql.Timestamp;

@Embeddable
public class Profile {
    private Timestamp joined;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;
}
