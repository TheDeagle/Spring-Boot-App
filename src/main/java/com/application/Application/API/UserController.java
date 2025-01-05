package com.application.Application.API;

import com.application.Application.Modules.Message;
import com.application.Application.Services.MessageServices;
import com.application.Application.Services.UserServices;
import com.application.Application.Modules.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class UserController {
    private final UserServices Users;
    private final MessageServices Messages;

    public UserController(UserServices userServices, MessageServices messageServices)
    {
        this.Users = userServices;
        this.Messages = messageServices;
    }

    @GetMapping("/api/v1/csrftoken")
    public ResponseEntity<Object> csrftoken(HttpServletRequest request){
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/api/v1/users")
    public ResponseEntity<Object> listUsers(HttpServletRequest request){
        HttpSession _session = request.getSession(false);
        if (_session == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need to login first!");
        return ResponseEntity.ok().body(Users.getAll());
    }

    @GetMapping("/api/v1/users/{username}")
    public ResponseEntity<Object> user(@PathVariable String username, HttpServletRequest request){
        User _user = this.Users.isLoggedIn(request);
        if (_user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need to login first!");
        User _ret = this.Users.getUser(username);
        if (_ret == null)
            return ResponseEntity.badRequest().body("User does not exist!");
        List<Object> _list = new ArrayList<>();
        _list.add(_ret);
        _list.add(this.Messages.getMessage(_user, _ret).getMessages());
        return ResponseEntity.ok().body(_list);
    }

    @PostMapping("/api/v1/register")
    public ResponseEntity<String> createUser(@RequestParam("username") String username,
                                             @RequestParam("password1") String password1,
                                             @RequestParam("password2") String password2,
                                             @RequestParam(value="pfp", required=false) MultipartFile file) throws IOException {
        User _user = this.Users.getUser(username);
        if (_user == null){
            if (!Objects.equals(password1, password2))
                return ResponseEntity.badRequest().body("password mismatch!");
            try {
                User user = new User(username, password1);
                if (!file.isEmpty()){
                    try {
                        Path path = Paths.get("uploads/" + file.getOriginalFilename());
                        Files.createDirectories(path.getParent());
                        file.transferTo(path);
                        user.setPfpPath(path.toString());
                    }
                    catch (IOException e){
                        return ResponseEntity.badRequest().body("Something went wrong during the image process!");
                    }
                }
                _user = this.Users.createUser(user);
            }
            catch (Exception e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("user with username " + _user.getUsername() + " has been created");
        }
        return ResponseEntity.badRequest().body("username already exists");
    }

    @GetMapping("/api/v1/profile")
    public ResponseEntity<Object> profile(HttpServletRequest request){
        User _user = this.Users.isLoggedIn(request);
        if (_user != null)
            return ResponseEntity.status(HttpStatus.OK).body(_user);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in!");
    }
}
