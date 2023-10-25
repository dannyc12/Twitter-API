package com.cooksys.assessment1team3.repositories;

import com.cooksys.assessment1team3.entities.Hashtag;
import com.cooksys.assessment1team3.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
    List<Tweet> findAllByDeletedFalseAndHashtagsOrderByPostedDesc(Hashtag hashtag);
}
