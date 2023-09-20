package com.workintech.twitter.service;

import com.workintech.twitter.dao.UserRepository;
import com.workintech.twitter.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findMemberByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("username not found"));
    }

    public User loadUserByUsernameReturnUser(String username) throws UsernameNotFoundException {
        return userRepository.findMemberByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("username not found"));
    }

}
