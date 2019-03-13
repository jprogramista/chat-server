package sample.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import sample.model.Message;
import sample.listeners.WsSessions;

import java.security.Principal;

import static java.util.Objects.nonNull;

@Controller
public class ChatController 
{
    
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private WsSessions userSessions;
    

    @RequestMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin("*")
    public void register(@RequestParam String username, @RequestParam String sessionId) {
        userSessions.put(username, sessionId);
    }
    
    @MessageMapping("/chat/user")
    public void sendTo(@Payload Message message, @Header("simpSessionId") String sessionId) throws Exception {
        /*
            could be a session or username depends how user subscribe to queue 
         */
        String session = userSessions.get(message.getTo());
        if (nonNull(session)) {
            message.setFrom(userSessions.getByValue(sessionId));
            simpMessagingTemplate.convertAndSend("/conversation/user-" + session, message);
        } else {
            simpMessagingTemplate.convertAndSend("/conversation/user-" + sessionId, 
                    new Message("system", userSessions.getByValue(sessionId), "Recipient not connected"));
        }
        //simpMessagingTemplate.convertAndSendToUser(message.getTo(), "/conversation", message);
    }

    @MessageMapping("/room/send/{roomId}")
    public void sentToRoom(@DestinationVariable String roomId, @Payload Message message, @Header("simpSessionId") String sessionId) {
        message.setFrom(userSessions.getByValue(sessionId));
        message.setTo(roomId);
        simpMessagingTemplate.convertAndSend("/room/" + roomId, message);
    }
}
