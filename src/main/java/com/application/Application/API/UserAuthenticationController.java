package com.application.Application.API;

import com.application.Application.Modules.User;
import com.application.Application.Services.UserServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserAuthenticationController {
    private final UserServices Users;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserAuthenticationController(UserServices userServices, AuthenticationManager authenticationManager){
        this.Users = userServices;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/api/v1/login")
    public ResponseEntity<String> loginUser(@RequestParam("username") String username,
                                            @RequestParam("password") String password,
                                            HttpServletRequest request){
        UsernamePasswordAuthenticationToken _token = new UsernamePasswordAuthenticationToken(username, password);
        try {
            Authentication authentication = authenticationManager.authenticate(_token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession _session = request.getSession(true);
            _session.setAttribute("username", username);
            return ResponseEntity.ok().body("User has logged in successfully");
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Invalid credentials!");
        }
    }

    @GetMapping("/api/v1/logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session != null){
            session.invalidate();
            return ResponseEntity.ok().body("User has logged out successfully");
        }
        return ResponseEntity.ok().body("User is already logged out");
    }
}
