package com.example.Backend.Config;

import com.example.Backend.Services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class Config {

    @Autowired
    @Lazy
    private UserServices userServices;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws RuntimeException{
        try {
            security
                    .httpBasic().disable()
                    .formLogin().disable()
                    .csrf().disable()
                    .cors().configurationSource(corsConfigurationSource());
            security.headers().frameOptions().disable();
            return security.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration _cors = new CorsConfiguration();
        _cors.setAllowedHeaders(Arrays.asList("*"));
        _cors.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE"));
        _cors.setAllowedOrigins(Arrays.asList("http://localhost:5500"));
        _cors.setAllowCredentials(true);
        _cors.addExposedHeader("Set-Cookie");
        UrlBasedCorsConfigurationSource _source = new UrlBasedCorsConfigurationSource();
        _source.registerCorsConfiguration("/**", _cors);
        return _source;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider _authenticationProvider = new DaoAuthenticationProvider();
        _authenticationProvider.setPasswordEncoder(passwordEncoder());
        _authenticationProvider.setUserDetailsService(this.userServices);
        return _authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        try {
            return configuration.getAuthenticationManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

