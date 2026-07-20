package com.url.shortner.controller;

import com.url.shortner.dtos.LoginRequest;
import com.url.shortner.dtos.RegisterRequest;
import com.url.shortner.models.User;
import com.url.shortner.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {

        this.userService = userService;
    }
    @PostMapping("/public/login")
    public ResponseEntity<?>loginUser(@RequestBody LoginRequest loginrequest)
    {
            return ResponseEntity.ok(userService.authenticateuser(loginrequest));
    }


    @PostMapping("/public/register")
    public ResponseEntity<?>registerUser(@RequestBody RegisterRequest registerRequest)
    {
        try{
            User user=new User();
            user.setUsername(registerRequest.getUsername());
            user.setPassword(registerRequest.getPassword());
            user.setEmail(registerRequest.getEmail());
            user.setRole("ROLE_USER");
            userService.registeruser(user);
            return ResponseEntity.ok("user registered successfully");
        }

        catch(RuntimeException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
