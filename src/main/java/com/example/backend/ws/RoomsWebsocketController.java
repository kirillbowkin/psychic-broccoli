package com.example.backend.ws;

import java.util.UUID;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class RoomsWebsocketController {

    @MessageMapping("/send/{roomId}")
    @SendTo("/topic/rooms/{roomId}")
    public String ping(@DestinationVariable UUID roomId) {
        return roomId.toString();
    }
}
