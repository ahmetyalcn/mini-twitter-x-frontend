package com.workintech.twitter.controller;

import com.workintech.twitter.dto.*;
import com.workintech.twitter.entity.Like;
import com.workintech.twitter.entity.Post;
import com.workintech.twitter.entity.Reply;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.service.UserService;
import com.workintech.twitter.util.TwitterUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/user")
public class ProfileController {
    private UserService userService;

    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ProfileResponse getProfile(){
        User owner = TwitterUtility.getUserFromToken(userService);
        List<PostResponse> postResponses=new ArrayList<>();
        Iterator<Post> postIterator = owner.getPosts().iterator();
        while (postIterator.hasNext()) {
            Post post = postIterator.next();
            List<LikeResponse> likeResponses = new ArrayList<>();
            List<ReplyResponse> replyResponses = new ArrayList<>();

            for (Like like: post.getLikes()){
                User user = like.getUser();
                likeResponses.add(new LikeResponse(user.getUsername()));
            }
            for (Reply reply: post.getReplies()){
                replyResponses.add(new ReplyResponse(reply.getId(), reply.getUser().getUsername(),reply.getContent()));
            }
            postResponses.add(new PostResponse(post.getId(),post.getUser().getUsername(),post.getTweetType(),
                    post.getTweet().getContent(), post.getPostDate(),likeResponses,replyResponses));
        }
        List<UserLikesResponse> userLikesResponses=new ArrayList<>();
        for (Like like: owner.getLikes()){
            userLikesResponses.add(new UserLikesResponse(like.getPost().getId()));
        }
        return new ProfileResponse(owner.getId(), owner.getUsername(), owner.getEmail(), postResponses,userLikesResponses);
    }
}
