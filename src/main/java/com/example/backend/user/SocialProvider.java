package com.example.backend.user;

import java.util.Map;

public interface SocialProvider {
    Map<String, String> getUserInfo();
}
