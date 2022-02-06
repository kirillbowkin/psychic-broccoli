package com.example.backend.dto;

import java.util.UUID;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WatchRoomDto {

    private String roomName;
    private UUID movieId;

}
