package com.example.backend.security;

import com.example.backend.user.GoogleSocialProvider;
import com.example.backend.user.User;
import com.example.backend.user.UserService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("IN attemptAuthentication");
        String social_login = request.getParameter("social_provider");
        String social_token = request.getParameter("social_token");

        if(social_login != null && social_token != null) {
            User user = userService.socialLogin(new GoogleSocialProvider(social_token));
            return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        }
        return null;

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("IN successfulAuthentication");
        super.successfulAuthentication(request, response, chain, authResult);
    }


}
