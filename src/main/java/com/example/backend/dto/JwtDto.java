package com.example.backend.dto;

import com.example.backend.model.user.User;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class JwtDto {

    private User user;
    private Map<String, String> tokens;
}
