package com.workintech.twitter.controller;

import com.workintech.twitter.dto.ReplyRequest;
import com.workintech.twitter.dto.ReplyResponse;
import com.workintech.twitter.entity.Post;
import com.workintech.twitter.entity.Reply;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.exceptions.TwitterExceptions;
import com.workintech.twitter.service.PostService;
import com.workintech.twitter.service.ReplyService;
import com.workintech.twitter.service.UserService;
import com.workintech.twitter.util.TwitterUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;

@RestController
@RequestMapping("/tweet/reply")
public class ReplyController {
    private ReplyService replyService;
    private UserService userService;
    private PostService postService;
    @Autowired
    public ReplyController(ReplyService replyService, UserService userService, PostService postService) {
        this.replyService = replyService;
        this.userService = userService;
        this.postService = postService;
    }

    @PostMapping("/{id}")
    public ReplyResponse save(@RequestBody ReplyRequest replyRequest, @PathVariable int id){
        Reply reply = new Reply();
        reply.setContent(replyRequest.content());

        User user = TwitterUtility.getUserFromToken(userService);

        Post post = postService.findById(id);
        reply.setUser(user);
        reply.setPost(post);
        post.addReply(reply);
        user.addReply(reply);

        replyService.save(reply);

        return new ReplyResponse(reply.getId(), reply.getUser().getUsername(),reply.getContent());
    }

    @DeleteMapping("/{replyId}")
    public ReplyResponse delete(@PathVariable int replyId) {
        User user = TwitterUtility.getUserFromToken(userService);

        Reply replyToDelete = null;

        Iterator<Reply> replyIterator = user.getReplies().iterator();
        while (replyIterator.hasNext()) {
            Reply reply = replyIterator.next();
            if (reply.getId() == replyId) {
                replyToDelete = reply;
                replyIterator.remove();
                break;
            }
        }

        if (replyToDelete != null) {
            replyService.delete(replyToDelete);
            return new ReplyResponse(replyToDelete.getId(), replyToDelete.getUser().getUsername(),
                    replyToDelete.getContent());
        }

        throw new TwitterExceptions("You can't delete this reply", HttpStatus.BAD_REQUEST);
    }
}
