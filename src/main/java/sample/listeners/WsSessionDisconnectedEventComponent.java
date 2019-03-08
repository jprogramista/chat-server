package sample.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WsSessionDisconnectedEventComponent implements ApplicationListener<SessionDisconnectEvent> {

    @Autowired
    private WsSessions userSessions;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        userSessions.remove(event.getUser().getName());
    }
    
}
