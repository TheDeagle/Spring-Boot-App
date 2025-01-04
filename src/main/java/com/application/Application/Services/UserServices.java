package com.application.Application.Services;

import com.application.Application.Modules.User;
import com.application.Application.Repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServices implements UserDetailsService {
    UserRepository Users;
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    UserServices(UserRepository userRepository){
        this.Users = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User _user = this.getUser(username);
        if (_user == null)
            throw new UsernameNotFoundException("User not found");
        return _user;
    }

    public User getUser(String username) {
        if (username == null)
            return null;
        return this.Users.findByUsername(username);
    }

    public User getUser(User user){
        if (user == null)
            return null;
        return this.getUser(user.getUsername());
    }

    public User deleteUser(User user){
        User _user = this.getUser(user);
        if (_user != null)
        {
            this.Users.delete(_user);
            return _user;
        }
        return null;
    }

    public User deleteUser(String username){
        User _user = this.getUser(username);
        if (_user != null)
        {
            this.Users.delete(_user);
            return _user;
        }
        return null;
    }

    public User createUser(User user) throws Exception{
        if (user.getUsername().strip().isBlank() && user.getPassword().strip().isBlank())
            throw new Exception("Please provide a valid username and password");
        if (user.getUsername() == null && user.getPassword() == null)
            throw new Exception("Please provide a valid username and password");
        if (user.getUsername().strip().isBlank())
            throw new Exception("Please provide a valid username");
        if (user.getPassword().strip().isBlank())
            throw new Exception("Please provide a valid password");
        if (user.getUsername() == null)
            throw new Exception("Please provide a valid username");
        if (user.getPassword() == null)
            throw new Exception("Please provide a valid password");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.Users.save(user);
    }

    public List<User> getAll(){
        return this.Users.findAll();
    }

    public User isLoggedIn(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session != null){
            String username = (String)session.getAttribute("username");
            return this.getUser(username);
        }
        return null;
    }

    public User isWebSocketLoggedIn(WebSocketSession session){
        Map<String, Object> _sessionAttributes = session.getAttributes();
        String username = (String)_sessionAttributes.get("username");
        if (_sessionAttributes.isEmpty() || username.isEmpty())
            return null;
        User _user = this.getUser(username);
        if (_user == null)
            return null;
        return _user;
    }

    public Optional<User> getUserBydId(long id){
        return this.Users.findById(id);
    }
}
