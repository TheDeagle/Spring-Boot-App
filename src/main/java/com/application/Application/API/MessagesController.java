package com.application.Application.API;

import com.application.Application.Modules.Message;
import com.application.Application.Modules.User;
import com.application.Application.Services.MessageServices;
import com.application.Application.Services.UserServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessagesController {
    private final MessageServices Messages;
    private final UserServices Users;

    @Autowired
    public MessagesController(MessageServices messageServices, UserServices userServices){
        this.Messages = messageServices;
        this.Users = userServices;
    }

    @GetMapping("api/v1/messages/{username}")
    public ResponseEntity<Object> messages(@PathVariable String username, HttpServletRequest request){
        User _user = this.Users.isLoggedIn(request);
        if (_user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need to login first!");
        User _user2 = this.Users.getUser(username);
        if (_user2 == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User does not exist!");
        Message _msg = this.Messages.getMessage(_user, _user2);
        if (_msg == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No messages between the two users!");
        return ResponseEntity.ok().body(_msg);
    }
}
