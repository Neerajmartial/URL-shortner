package com.url.shortner.service;

import com.url.shortner.Repository.UserRepository;
import com.url.shortner.dtos.LoginRequest;
import com.url.shortner.models.User;
import com.url.shortner.security.JwtAuthenticationResponse;
import com.url.shortner.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final PasswordEncoder passwordEncode;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;


    public UserService(PasswordEncoder passwordEncode, UserRepository userRepository,AuthenticationManager authenticationManager,JwtUtils jwtUtils) {
        this.passwordEncode = passwordEncode;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils=jwtUtils;
    }

    public  User findByUsername(String name) {
            return userRepository.findByUsername(name).orElseThrow(
                    ()->new UsernameNotFoundException("User not found with given username"+name)
            );
    }

    public JwtAuthenticationResponse authenticateuser(LoginRequest loginRequest)
    {
        Authentication authentication=authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                    loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails= (UserDetailsImpl) authentication.getPrincipal();
        String jwt=jwtUtils.generateToken(userDetails);
        return new JwtAuthenticationResponse(jwt);
    }

    public void registeruser(User user)
    {
        Optional<User>existUser=userRepository.findByUsername(user.getUsername());
        if(existUser.isPresent())
        {
            throw new RuntimeException("User already exist");
        }
        user.setPassword(passwordEncode.encode(user.getPassword()));
        userRepository.save(user);
    }
}
