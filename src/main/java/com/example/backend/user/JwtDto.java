package com.example.backend.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class JwtDto {

    private User user;
    private String access_token;
    private String refresh_token;
}
