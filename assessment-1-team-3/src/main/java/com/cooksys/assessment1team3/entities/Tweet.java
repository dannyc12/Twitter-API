package com.cooksys.assessment1team3.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author", referencedColumnName = "id")
    private User author;

    private Timestamp posted;

    private boolean deleted;

    private String content;

    @ManyToOne
    private Tweet inReplyTo;

    @OneToMany(mappedBy = "inReplyTo")
    private List<Tweet> replies;

    @ManyToOne
    private Tweet repostOf;

    @OneToMany(mappedBy = "repostOf")
    private List<Tweet> reposts;

    @ManyToMany(mappedBy = "tweets")
    private List<Hashtag> hashtags;

    @ManyToMany(mappedBy = "userLikes")
    private List<User> userLikes;

    @ManyToMany(mappedBy = "userMentions")
    private List<User> userMentions;

}
