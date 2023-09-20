package com.workintech.twitter.service;

import com.workintech.twitter.entity.Like;
import com.workintech.twitter.entity.Reply;

public interface ReplyService {
    Reply findReplyById(int id);
    Reply save(Reply reply);
    void delete(Reply reply);
}
