package sample.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sample.model.Message;
import sample.listeners.WsSessions;
import sample.service.ChatHistoryService;

import java.security.Principal;
import java.util.List;

import static java.util.Objects.nonNull;

@Controller
public class ChatController 
{
    
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final WsSessions userSessions;
    
    private final ChatHistoryService chatHistoryService;

    public ChatController(SimpMessagingTemplate simpMessagingTemplate, WsSessions userSessions, ChatHistoryService chatHistoryService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userSessions = userSessions;
        this.chatHistoryService = chatHistoryService;
    }

    @MessageMapping("/chat/user")
    public void sendTo(@Payload Message message, Principal user, @Header("simpSessionId") String sessionId) throws Exception {
        /*
            could be a session or username depends how user subscribe to queue 
         */
        String session = userSessions.get(message.getTo());
        if (nonNull(session)) {
            message.setFrom(user.getName());
            simpMessagingTemplate.convertAndSend("/conversation/user-" + session, message);
            chatHistoryService.addMessage(message);
        } else {
            simpMessagingTemplate.convertAndSend("/conversation/user-" + sessionId, 
                    new Message("system", user.getName(), "Recipient not connected"));
        }
        //simpMessagingTemplate.convertAndSendToUser(message.getTo(), "/conversation", message);
    }

    @MessageMapping("/room/send/{roomId}")
    public void sentToRoom(@DestinationVariable String roomId, @Payload Message message, Principal user) {
        message.setFrom(user.getName());
        message.setTo(roomId);
        simpMessagingTemplate.convertAndSend("/room/" + roomId, message);
    }
    
    @GetMapping("/history")
    public @ResponseBody List<Message> getHistory(@RequestParam String recipient, Principal user) {
        return chatHistoryService.getHistory(user.getName(), recipient);        
    } 
    
}
