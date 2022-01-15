package com.example.backend.social;

import java.util.Map;

public interface SocialProvider {
    Map<String, String> getUserInfo();
}
