package com.workintech.twitter.util;

import com.workintech.twitter.dto.LikeResponse;
import com.workintech.twitter.dto.PostResponse;
import com.workintech.twitter.dto.ReplyResponse;
import com.workintech.twitter.entity.Like;
import com.workintech.twitter.entity.Post;
import com.workintech.twitter.entity.Reply;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.List;

public class TwitterUtility {
    public static User getUserFromToken(UserService userService){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getClaim("sub").toString();
        User owner =  userService.loadUserByUsernameReturnUser(username);
        return owner;
    }

    public static PostResponse postResponseReturner(Post post){
        List<LikeResponse> likeResponses = new ArrayList<>();
        List<ReplyResponse> replyResponses = new ArrayList<>();
        for (Like like: post.getLikes()){
            User user = like.getUser();
            likeResponses.add(new LikeResponse(user.getUsername()));
        }
        for (Reply reply: post.getReplies()){
            replyResponses.add(new ReplyResponse(reply.getId(), reply.getUser().getUsername(),reply.getContent()));
        }
        return TwitterUtility.postResponse(post,likeResponses,replyResponses);
    }

    public static List<LikeResponse> likesReturn(Post post){
        List<LikeResponse> likeResponses = new ArrayList<>();
        for (Like like: post.getLikes()){
            User user = like.getUser();
            likeResponses.add(new LikeResponse(user.getUsername()));
        }
        return likeResponses;
    }
    public static List<ReplyResponse> repliesReturn(Post post){
        List<ReplyResponse> replyResponses = new ArrayList<>();
        for (Reply reply: post.getReplies()){
            replyResponses.add(new ReplyResponse(reply.getId(), reply.getUser().getUsername(),reply.getContent()));
        }
        return replyResponses;
    }

    public static PostResponse postResponse(Post post, List<LikeResponse> likeResponses, List<ReplyResponse> replyResponses){
        return new PostResponse(post.getId(),post.getUser().getUsername(),
                post.getTweetType(),post.getTweet().getContent(),post.getPostDate(),likeResponses,replyResponses);
    }
}
