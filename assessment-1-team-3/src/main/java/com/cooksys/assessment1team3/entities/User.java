package com.cooksys.assessment1team3.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean deleted = false;

    @Embedded
    private Profile profile;

    @Embedded
    private Credentials credentials;

    @OneToMany
    private List<Tweet> tweets;

    @ManyToMany
    @JoinTable(
            name="followers_following",
            joinColumns=@JoinColumn(name="follower_id"),
            inverseJoinColumns=@JoinColumn(name="following_id")
    )
    private List<User> followers;

    @ManyToMany
    @JoinTable(
    name="followers_following",
    joinColumns=@JoinColumn(name="following_id"),
    inverseJoinColumns=@JoinColumn(name="follower_id")
    )
    private List<User> following;


    @ManyToMany
    @JoinTable(name = "user_likes",
            joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "tweet_id"))
    private List<Tweet> userLikes;

    @ManyToMany
    @JoinTable(name = "user_mentions",
            joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "tweet_id"))
    private List<Tweet> userMentions;

}