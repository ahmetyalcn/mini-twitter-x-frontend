package com.workintech.twitter.service;

import com.workintech.twitter.dao.PostRepository;
import com.workintech.twitter.entity.Post;
import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.exceptions.TwitterExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public List<Post> getAllTweetsInDescOrder() {
        return postRepository.getAllTweetsInDescOrder();
    }

    @Override
    public Post findById(int id) {
        Optional<Post> founded = postRepository.findById(id);
        if (founded.isPresent()){
            return founded.get();
        }
        throw new TwitterExceptions("Id not found", HttpStatus.BAD_REQUEST);
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public void delete(Post post) {
       postRepository.delete(post);
    }


}
