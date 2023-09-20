package com.workintech.twitter.service;

import com.workintech.twitter.entity.Post;
import com.workintech.twitter.entity.Tweet;

import java.util.List;

public interface PostService {
    List<Post> findAll();
    List<Post> getAllTweetsInDescOrder();
    Post findById(int id);
    Post save(Post post);
    void delete(Post post);


}
