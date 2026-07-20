package com.url.shortner.service;


import com.url.shortner.models.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;

@Data
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String email;
    private String password;
    private String username;
    private Collection<? extends GrantedAuthority>authorities;

    public UserDetailsImpl(Long id, String password, String email, Collection<? extends GrantedAuthority> authorities, String username) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
        this.username = username;
    }



    public static UserDetailsImpl build(User user)
    {
        GrantedAuthority authority=new SimpleGrantedAuthority(user.getRole());
        return new UserDetailsImpl(
                user.getId(),
                user.getPassword(),
                user.getEmail(),
                Collections.singletonList(authority),
                user.getUsername()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}

