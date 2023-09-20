package com.workintech.twitter.service;

import com.workintech.twitter.dao.LikeRepository;
import com.workintech.twitter.entity.Like;
import com.workintech.twitter.entity.Post;
import com.workintech.twitter.exceptions.TwitterExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeServiceImpl implements LikeService{
    private LikeRepository likeRepository;
    @Autowired
    public LikeServiceImpl(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @Override
    public Like findLikeById(int id) {
        Optional<Like> founded = likeRepository.findById(id);
        if (founded.isPresent()){
            return founded.get();
        }
        throw new TwitterExceptions("Id not found", HttpStatus.BAD_REQUEST);
    }

    @Override
    public Like like(Like like) {
        return likeRepository.save(like);
    }

    @Override
    public void unlike(Like like) {
        likeRepository.delete(like);
    }

}
