package sample.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
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
    

    @MessageMapping("/chat/user")
    public void sendTo(@Payload Message message, Principal user, @Header("simpSessionId") String sessionId) throws Exception {
        /*
            could be a session or username depends how user subscribe to queue 
         */
        String session = userSessions.get(message.getTo());
        if (nonNull(session)) {
            message.setFrom(user.getName());
            simpMessagingTemplate.convertAndSend("/conversation/user-" + session, message);
        } else {
            simpMessagingTemplate.convertAndSend("/conversation/user-" + sessionId, 
                    new Message("system", user.getName(), "Recipient not connected"));
        }
        //simpMessagingTemplate.convertAndSendToUser(message.getTo(), "/conversation", message);
    }
}
