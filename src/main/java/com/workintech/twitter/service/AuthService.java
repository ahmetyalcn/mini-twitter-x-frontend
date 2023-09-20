package com.workintech.twitter.service;


import com.workintech.twitter.dao.RoleRepository;
import com.workintech.twitter.dao.UserRepository;
import com.workintech.twitter.dto.LoginResponse;
import com.workintech.twitter.dto.UserResponse;
import com.workintech.twitter.entity.Role;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.exceptions.TwitterExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthService {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private AuthenticationManager authenticationManager;
    private TokenService tokenService;

    @Autowired
    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepository,
                                 RoleRepository roleRepository, AuthenticationManager authenticationManager,
                                 TokenService tokenService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }



    public User register(String username, String password ,String email){
        Optional<User> foundMember = userRepository.findMemberByUsername(username);
        if(foundMember.isPresent()){
            throw new TwitterExceptions("This username already exist!", HttpStatus.BAD_REQUEST);
        }

        String encodedPassword = passwordEncoder.encode(password);
        Role memberRole = roleRepository.findByAuthority("USER").get();


        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setEmail(email);
        user.setAuthority(memberRole);
        return userRepository.save(user);
    }

    public LoginResponse login(String username, String password){
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
            String token = tokenService.generateJwtToken(auth);
            User user = userRepository.findMemberByUsername(username).get();
            return new LoginResponse(new UserResponse(user.getId(), user.getUsername(),user.getPassword(),user.getEmail(),user.getAuthority().getAuthority()),token);

        }catch (AuthenticationException ex){
            throw new TwitterExceptions("Invalid login information!", HttpStatus.BAD_REQUEST);
        }
    }
}