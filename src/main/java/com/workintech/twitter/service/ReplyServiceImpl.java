package com.workintech.twitter.service;

import com.workintech.twitter.dao.ReplyRepository;
import com.workintech.twitter.entity.Like;
import com.workintech.twitter.entity.Reply;
import com.workintech.twitter.exceptions.TwitterExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReplyServiceImpl implements ReplyService{
    private ReplyRepository replyRepository;

    @Autowired
    public ReplyServiceImpl(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    @Override
    public Reply findReplyById(int id) {
        Optional<Reply> founded = replyRepository.findById(id);
        if (founded.isPresent()){
            return founded.get();
        }
        throw new TwitterExceptions("Id not found", HttpStatus.BAD_REQUEST);
    }

    @Override
    public Reply save(Reply reply) {
        return replyRepository.save(reply);
    }

    @Override
    public void delete(Reply reply) {
        replyRepository.delete(reply);
    }
}
