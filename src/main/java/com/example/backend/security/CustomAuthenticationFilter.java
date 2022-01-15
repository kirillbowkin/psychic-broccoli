package com.example.backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.backend.social.GoogleSocialProvider;
import com.example.backend.dto.JwtDto;
import com.example.backend.model.user.User;
import com.example.backend.model.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final Algorithm algorithm;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("IN attemptAuthentication");
        String social_login = request.getParameter("social_provider");
        String social_token = request.getParameter("social_token");
        String refresh = request.getParameter("refresh");

        if (social_login != null && social_token != null) {
            User user = userService.socialLogin(new GoogleSocialProvider(social_token));
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), "");
            return authenticationManager.authenticate(authenticationToken);
        }

        if (refresh != null) {
            try {
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh);
                String username = decodedJWT.getSubject();

                Optional<User> user = userService.getUser(username);
                if (user.isPresent()) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.get().getUsername(), "");
                    return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
                }
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid token");
            }
        }
        return null;

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        List<String> roles = authResult.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        String access_token = JWT.create()
                .withSubject(authResult.getName())
                .withClaim("roles", roles)
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) // 10 mins
                .sign(algorithm);

        String refresh_token = JWT.create()
                .withSubject(authResult.getName())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000)) // 30 mins
                .sign(algorithm);


        Optional<User> optionalUser = userService.getUser(authResult.getName());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            JwtDto jwtDto = new JwtDto().setUser(user).setAccess_token(access_token).setRefresh_token(refresh_token);

            log.info("IN successfulAuthentication - user {} successfully authenticated", user.getProfileName());
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getWriter(), jwtDto);
        }
    }


}
