package com.cooksys.assessment1team3.repositories;


import com.cooksys.assessment1team3.dtos.TweetResponseDto;
import com.cooksys.assessment1team3.entities.Hashtag;
import com.cooksys.assessment1team3.entities.Tweet;
import com.cooksys.assessment1team3.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
    List<Tweet> findAllTweetsByAuthorAndDeletedIsFalseOrderByPostedDesc(User author);

    List<Tweet> findAllByDeletedFalseAndHashtagsOrderByPostedDesc(Hashtag hashtag);

    List<Tweet> findAllByDeletedFalseOrderByPostedDesc();

    @Query("SELECT t FROM Tweet t WHERE t.inReplyTo.id = :tweetId AND t.deleted = false")
    List<Tweet> findAllRepliesToTweet(Long tweetId);

    Tweet findByIdAndDeletedFalse(Long id);
}
