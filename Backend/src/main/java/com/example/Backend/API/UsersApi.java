package com.example.Backend.API;


import com.example.Backend.Modules.Messages;
import com.example.Backend.Modules.User;
import com.example.Backend.Services.JWTServices;
import com.example.Backend.Services.MessagesServices;
import com.example.Backend.Services.UserServices;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.List;

@RestController
public class UsersApi {
    private final UserServices userServices;
    private final JWTServices jwtServices;
    private final MessagesServices messagesServices;

    @Autowired
    public UsersApi(UserServices userServices, JWTServices jwtServices, MessagesServices messagesServices)
    {
        this.userServices = userServices;
        this.jwtServices = jwtServices;
        this.messagesServices = messagesServices;
    }

    @PostMapping("/api/users/register")
    ResponseEntity<Object> register(@RequestParam("username") String username,
                                    @RequestParam("password")  String password){
        if (this.userServices.getUser(username) != null)
            return ResponseEntity.badRequest().body("This username already exists!");
        if (!username.matches("^[a-zA-Z0-9_.]{3,30}$"))
            return ResponseEntity.badRequest().body("The username must be between 3 and 30 characters and alphanumeric characters or underscore!");
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{4,20}$"))
            return ResponseEntity.badRequest().body("The password should be between 4 and 20 characters and should contain at least one uppercase letter, one lowercase letter and one digit!");
        User _user = userServices.createUser(username, password);
        if (_user == null)
            return ResponseEntity.badRequest().body("Please provide a valid username and password!");
        return ResponseEntity.status(HttpStatus.CREATED).body("A user with username " + _user.getUsername() + " has been created successfully!");
    }

    @PostMapping("/api/users/login")
    public ResponseEntity<Object> login(@RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 HttpServletResponse response){
        if (username == null || username.strip().isBlank() || password == null || password.strip().isBlank())
            return ResponseEntity.badRequest().body("Please provide a valid username or password!");
        if (!this.userServices.authenticateUser(username, password))
            return ResponseEntity.badRequest().body("The username or password are incorrect!");
        Cookie _cookie = new Cookie("AccessToken", this.jwtServices.generateToken(username));
        _cookie.setHttpOnly(true);
        _cookie.setPath("/");
        
        response.addCookie(_cookie);
        return ResponseEntity.accepted().body("You have logged in successfully!");
    }

    @GetMapping("/api/users/me")
    public ResponseEntity<Object> me(HttpServletRequest request){
        User _currentUser = this.userServices.currentUser(request);
        if (_currentUser == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You're not logged in!");
        }
        return ResponseEntity.ok().body(_currentUser);
    }

    @GetMapping("/api/users/all")
    public ResponseEntity<Object> all(HttpServletRequest request){
        User _currentUser = this.userServices.currentUser(request);
        if (_currentUser == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You're not logged in!");
        List<User> _all = this.userServices.all();
        if (_all == null)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("There's no users at the moment");
        return ResponseEntity.ok().body(_all);
    }

    @GetMapping("/api/users/{username}")
    public ResponseEntity<Object> user(@PathVariable String username, HttpServletRequest request){
        if (this.userServices.currentUser(request) == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in!");
        User _user = this.userServices.getUser(username);
        if (_user == null)
            return ResponseEntity.badRequest().body("User does not exist!");
        return ResponseEntity.ok().body(_user);
    }

    @GetMapping("/api/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request, HttpServletResponse response){
        if (this.userServices.currentUser(request) == null)
            return ResponseEntity.badRequest().body("You're already logged out!");
        Cookie _cookie = new Cookie("AccessToken", "");
        _cookie.setHttpOnly(true);
        _cookie.setPath("/");
        response.addCookie(_cookie);
        return ResponseEntity.ok().body("You have logged out successfully!");
    }

    @GetMapping("/api/messages/{username}")
    public ResponseEntity<Object> messages(@PathVariable String username,  HttpServletRequest request){
        User _currentUser = this.userServices.currentUser(request);
        User _otherUser = this.userServices.getUser(username);
        if (_currentUser == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in!");
        if (_otherUser == null)
            return ResponseEntity.badRequest().body("There's no such user!");
        Messages _msg = this.messagesServices.getMessage(_currentUser, _otherUser);
        if (_msg == null)
            _msg = this.messagesServices.getMessage(_otherUser, _currentUser);
        if (_msg == null)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Try starting a conversation with " + _otherUser.getUsername());
        return ResponseEntity.ok().body(_msg);
    }
}

