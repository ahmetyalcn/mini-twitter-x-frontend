package com.workintech.twitter.dto;

import com.workintech.twitter.entity.TweetType;

import java.time.LocalDate;
import java.util.List;

public record ProfileResponse (int id, String username,String email,List<PostResponse> posts,List<UserLikesResponse> userLikesResponses){
}
