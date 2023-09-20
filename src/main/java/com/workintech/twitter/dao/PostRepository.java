package com.workintech.twitter.dao;

import com.workintech.twitter.entity.Post;
import com.workintech.twitter.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("SELECT p FROM Post p ORDER BY p.postDate DESC")
    List<Post> getAllTweetsInDescOrder();
}
