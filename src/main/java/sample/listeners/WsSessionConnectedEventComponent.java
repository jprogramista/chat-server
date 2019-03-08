package sample.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;
import java.util.Map;

@Component
public class WsSessionConnectedEventComponent implements ApplicationListener<SessionConnectedEvent> {

    @Autowired
    private WsSessions userSessions;

    @Override
    public void onApplicationEvent(SessionConnectedEvent event) {
        userSessions.put(event.getUser().getName(), (String) event.getMessage().getHeaders().get("simpSessionId"));
    }

}
