package com.url.shortner.service;

import com.url.shortner.Repository.UserRepository;
import com.url.shortner.models.User;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    UserRepository userRepository;
    public UserDetailsServiceImpl(UserRepository userRepository)
    {
        this.userRepository=userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found with Username:"+username));
        return UserDetailsImpl.build(user);
    }
}
