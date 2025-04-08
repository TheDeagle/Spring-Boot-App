package com.example.Backend.Services;

import com.example.Backend.Modules.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.Backend.Repositories.UserRepository;

import java.util.List;

@Service
public class UserServices implements UserDetailsService {
    final private UserRepository userRepository;
    final private BCryptPasswordEncoder passwordEncoder;
    final private AuthenticationManager authenticationManager;
    final private JWTServices jwtServices;

    @Autowired
    public UserServices(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
                        AuthenticationManager authenticationManager, JWTServices jwtServices) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtServices = jwtServices;
    }

    public User createUser(String username, String password) {
        User _user = new User();
        if (username == null || username.strip().isBlank())
            return null;
        if (password == null || password.strip().isBlank())
            return null;
        _user.setUsername(username);
        _user.setPassword(passwordEncoder.encode(password));
        try {
            userRepository.save(_user);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        return _user;
    }

    public boolean save(User _user){
        try{
            if (_user == null)
                return false;
            this.userRepository.save(_user);
            return true;
        }
        catch (Exception e) {
            System.out.print(e.getMessage());
            return false;
        }
    }

    public User getUser(String username){
        return userRepository.findByUsername(username);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return this.userRepository.findByUsername(username);
        }
        catch (UsernameNotFoundException e){
            throw e;
        }
    }

    public List<User> all(){
        List<User> _all = this.userRepository.findAll();
        if (_all.isEmpty())
            return null;
        return _all;
    }

    public boolean authenticateUser(String username, String password) {
        try {
            Authentication _auth =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            if (_auth.isAuthenticated())
                return true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public User currentUser(HttpServletRequest request){
        String _token = request.getHeader("cookie");
        if (_token == null || _token.strip().isBlank() || _token.strip().isEmpty())
            return null;
        _token = _token.replace("AccessToken=", "");
        if (_token.strip().isBlank())
            return null;
        if (jwtServices.validateToken(_token)){
            String _username =  jwtServices.extractJwtUsername(_token);
            return this.getUser(_username);
        }
        return null;
    }

}

