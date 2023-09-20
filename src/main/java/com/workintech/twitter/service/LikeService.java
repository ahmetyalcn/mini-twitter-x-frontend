package com.workintech.twitter.service;

import com.workintech.twitter.entity.Like;
import com.workintech.twitter.entity.Post;

public interface LikeService {
    Like findLikeById(int it);
    Like like(Like like);
    void unlike(Like like);
}
