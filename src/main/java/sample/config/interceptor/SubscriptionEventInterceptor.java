package sample.config.interceptor;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

import java.security.Principal;
import java.util.Set;

public class SubscriptionEventInterceptor extends ChannelInterceptorAdapter {

    private static final String CONVERSATION_USER = "/conversation/user-";
    private static final String ROOM_PREFIX = "/room/";

    private Set<String> validRoomIds = Sets.newHashSet("room1", "room2", "room3");

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor headerAccessor= StompHeaderAccessor.wrap(message);
        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            Principal userPrincipal = headerAccessor.getUser();
            String sessionId = headerAccessor.getSessionId();
            String destination = headerAccessor.getDestination();

            if (destination.startsWith(CONVERSATION_USER)) {
                if (!StringUtils.equals(destination, CONVERSATION_USER + sessionId)) {
                    throw new IllegalArgumentException("No permission for connect to this queue");
                }
            } else if (destination.startsWith(ROOM_PREFIX)) {
                if (!validRoomSubscription(destination.replace(ROOM_PREFIX, ""))) {
                    throw new IllegalArgumentException("No room for subscribe");
                }
            }
        }
        return message;
    }

    private boolean validRoomSubscription(String roomId) {
        return validRoomIds.contains(roomId);
    }
}
