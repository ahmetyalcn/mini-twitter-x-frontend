package com.workintech.twitter.controller;

import com.workintech.twitter.dto.LikeResponse;
import com.workintech.twitter.dto.PostResponse;
import com.workintech.twitter.dto.ReplyResponse;
import com.workintech.twitter.dto.TweetRequest;
import com.workintech.twitter.entity.*;
import com.workintech.twitter.exceptions.TwitterExceptions;
import com.workintech.twitter.service.PostService;
import com.workintech.twitter.service.UserService;
import com.workintech.twitter.util.TwitterUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/tweet")
public class PostController {
    private PostService postService;
    private UserService userService;
    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/")
    public List<PostResponse> findAll(){
        List<Post> posts = postService.getAllTweetsInDescOrder();
        List<PostResponse> postResponses=new ArrayList<>();

        for (Post post: posts){
            List<LikeResponse> likeResponses = TwitterUtility.likesReturn(post);
            List<ReplyResponse> replyResponses = TwitterUtility.repliesReturn(post);
            postResponses.add(TwitterUtility.postResponse(post,likeResponses,replyResponses));
        }

        return postResponses;
    }

    @GetMapping("/{id}")
    public PostResponse find(@PathVariable int id){
        Post post = postService.findById(id);
        return TwitterUtility.postResponseReturner(post);
    }

    @PostMapping("/")
    public PostResponse save(@RequestBody TweetRequest tweetRequest){
        Post post = new Post();
        Tweet tweet = new Tweet();

        tweet.setContent(tweetRequest.content());
        tweet.setPost(post);

        post.setTweetType(TweetType.TWEET);
        post.setPostDate(LocalDateTime.now());

        User user = TwitterUtility.getUserFromToken(userService);

        user.addPost(post);
        post.setUser(user);
        post.setTweet(tweet);

        List<LikeResponse> likeResponses = new ArrayList<>();
        List<ReplyResponse> replyResponses = new ArrayList<>();
        postService.save(post);
        return TwitterUtility.postResponse(post,likeResponses,replyResponses);
    }

    @PostMapping("/retweet/{id}")
    public PostResponse saveRetweet(@PathVariable int id){

        Post post= postService.findById(id);

        User user = TwitterUtility.getUserFromToken(userService);

        Post newPost = new Post();
        Tweet tweet = new Tweet();

        tweet.setContent(post.getTweet().getContent());
        tweet.setPost(newPost);

        newPost.setTweetType(TweetType.RETWEET);
        newPost.setTweet(tweet);
        newPost.setPostDate(LocalDateTime.now());
        newPost.setUser(user);

        user.addPost(newPost);


        List<LikeResponse> likeResponses = new ArrayList<>();
        List<ReplyResponse> replyResponses = new ArrayList<>();
        postService.save(newPost);
        return TwitterUtility.postResponse(post,likeResponses,replyResponses);
    }


    @PutMapping("/{id}")
    public PostResponse update(@RequestBody TweetRequest tweetRequest, @PathVariable int id){
        User owner = TwitterUtility.getUserFromToken(userService);

        Post foundedPost = postService.findById(id);
        foundedPost.getTweet().setContent(tweetRequest.content());
        foundedPost.setPostDate(LocalDateTime.now());

        Post postToUpdate = null;
        Iterator<Post> postIterator = owner.getPosts().iterator();
        while (postIterator.hasNext()) {
            Post post = postIterator.next();
            if (post.getId() == id) {
                postToUpdate = post;
                postIterator.remove();
                break;
            }
        }
        List<LikeResponse> likeResponses = TwitterUtility.likesReturn(foundedPost);
        List<ReplyResponse> replyResponses = TwitterUtility.repliesReturn(foundedPost);

        if (postToUpdate != null) {
            postService.save(postToUpdate);
            return TwitterUtility.postResponse(foundedPost,likeResponses,replyResponses);
        }
        throw new TwitterExceptions("You can't update this post", HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping("/{id}")
    public PostResponse delete(@PathVariable int id){
        User user =  TwitterUtility.getUserFromToken(userService);
        Post foundedPost = postService.findById(id);

        Post postToDelete = null;
        Iterator<Post> postIterator = user.getPosts().iterator();
        while (postIterator.hasNext()) {
            Post post = postIterator.next();
            if (post.getId() == id) {
                postToDelete = post;
                postIterator.remove();
                break;
            }
        }

        if (postToDelete != null) {
            postService.delete(postToDelete);
            return TwitterUtility.postResponse(foundedPost,null,null);
        }

        throw new TwitterExceptions("You can't delete this post", HttpStatus.BAD_REQUEST);

    }
}
