package com.cooksys.assessment1team3.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String label;

    private Timestamp firstUsed;

    private Timestamp lastUsed;

    @ManyToMany
    @JoinTable(name="tweet_hashtags",
            joinColumns = @JoinColumn(name="tweet_id"), inverseJoinColumns = @JoinColumn(name = "hashtag_id"))
    private List<Tweet> tweets;

}
