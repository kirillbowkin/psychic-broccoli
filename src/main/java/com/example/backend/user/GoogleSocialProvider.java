package com.example.backend.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class GoogleSocialProvider implements SocialProvider {
    private String token;

    public GoogleSocialProvider(String token) {
        this.token = token;
    }

    @Override
    public Map<String, String> getUserInfo() {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> forObject = restTemplate.getForObject("https://www.googleapis.com/oauth2/v1/userinfo?access_token="+this.token, HashMap.class);
        return forObject;
    }
}
