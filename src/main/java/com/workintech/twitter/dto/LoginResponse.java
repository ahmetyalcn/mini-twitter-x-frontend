package com.workintech.twitter.dto;

import com.workintech.twitter.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private UserResponse userResponse;
    private String jwt;
}