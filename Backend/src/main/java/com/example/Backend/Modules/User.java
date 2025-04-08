package com.example.Backend.Modules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.WebSocketSession;

import java.io.File;
import java.util.Collection;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    Long id;
    @Pattern(regexp = "^[a-zA-Z0-9_.]{3,30}$", message = "The username must be between 3 and 30 characters and alphanumeric characters or underscore!")
    String username;
    @JsonIgnore
    String password;
    String pfpPath;


    public User() {

    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public User(Long id, String username, String password, String pfpPath){
        this.id = id;
        this.username = username;
        this.password = password;
        this.pfpPath = pfpPath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPfpPath(){
        return this.pfpPath;
    }

    public void setPfpPath(String pfpPath){
        this.pfpPath = pfpPath;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

}
