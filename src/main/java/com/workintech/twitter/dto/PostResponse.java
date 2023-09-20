package com.workintech.twitter.dto;

import com.workintech.twitter.entity.Like;
import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.entity.TweetType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record PostResponse(int id, String username, TweetType tweetType, String content, LocalDateTime date, List<LikeResponse> likes, List<ReplyResponse> responses) {
}
