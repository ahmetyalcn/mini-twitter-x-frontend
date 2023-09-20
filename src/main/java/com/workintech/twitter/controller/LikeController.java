package com.workintech.twitter.controller;

import com.workintech.twitter.dto.LikeResponse;
import com.workintech.twitter.dto.PostResponse;
import com.workintech.twitter.dto.ReplyResponse;
import com.workintech.twitter.entity.Like;
import com.workintech.twitter.entity.Post;
import com.workintech.twitter.entity.Reply;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.exceptions.TwitterExceptions;
import com.workintech.twitter.service.LikeService;
import com.workintech.twitter.service.PostService;
import com.workintech.twitter.service.UserService;
import com.workintech.twitter.util.TwitterUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/tweet/like")
public class LikeController {
    private LikeService likeService;
    private PostService postService;
    private UserService userService;
    @Autowired
    public LikeController(LikeService likeService, PostService postService, UserService userService) {
        this.likeService = likeService;
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping("/{id}")
    public PostResponse like(@PathVariable int id){
        Like like = new Like();

        Post post = postService.findById(id);

        User user = TwitterUtility.getUserFromToken(userService);

        for (Like l: post.getLikes()){
            if (l.getUser().getUsername()==user.getUsername()){
                throw new TwitterExceptions("Already liked",HttpStatus.BAD_REQUEST);
            }
        }

        like.setUser(user);
        like.setPost(post);

        post.addLike(like);
        user.addLike(like);
        List<LikeResponse> likeResponses = new ArrayList<>();
        List<ReplyResponse> replyResponses = new ArrayList<>();
        for (Like l: post.getLikes()){
            User u = l.getUser();
            likeResponses.add(new LikeResponse(u.getUsername()));
        }
        for (Reply reply: post.getReplies()){
            replyResponses.add(new ReplyResponse(reply.getId(), reply.getUser().getUsername(),reply.getContent()));
        }
        likeService.like(like);
        return new PostResponse(post.getId(),post.getUser().getUsername(),
                post.getTweetType(),post.getTweet().getContent(),post.getPostDate(),likeResponses,replyResponses);
    }

    @DeleteMapping("/{id}")
    public LikeResponse unlike(@PathVariable int id) {
        User user = TwitterUtility.getUserFromToken(userService);

        Like likeToDelete = null;

        Iterator<Like> likeIterator = user.getLikes().iterator();
        while (likeIterator.hasNext()) {
            Like like = likeIterator.next();
            if (like.getPost().getId() == id) {
                likeToDelete = like;
                likeIterator.remove();
                break;
            }
        }

        if (likeToDelete != null) {
            likeService.unlike(likeToDelete);
            return new LikeResponse("Post "+id+" unliked by"+ user.getUsername());
        }

        throw new TwitterExceptions("You can't delete this reply", HttpStatus.BAD_REQUEST);
    }

}
