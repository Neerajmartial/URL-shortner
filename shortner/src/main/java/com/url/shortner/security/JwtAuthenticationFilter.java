package com.url.shortner.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtutilsprovider;
    @Autowired
    private UserDetailsService userDetailsService;
    public JwtAuthenticationFilter(JwtUtils jwtutils)
    {
        this.jwtutilsprovider=jwtutils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try{
            //Get jwt from Header
           String jwt=jwtutilsprovider.getJwtFromHeader(request);
           //Validate Token
           if(jwt!=null && jwtutilsprovider.validateToken(jwt))
           {
               String username=jwtutilsprovider.getUsernameFromJwtToken(jwt);
               UserDetails userdetails= userDetailsService.loadUserByUsername(username);
               if(userdetails!=null)
               {
                   UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken
                           (userdetails,null,userdetails.getAuthorities());
                   authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                   SecurityContextHolder.getContext().setAuthentication(authentication);
               }
           }


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        filterChain.doFilter(request,response);
    }
}
