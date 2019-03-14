package sample.service;

import org.springframework.stereotype.Service;
import sample.model.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.nonNull;

@Service
public class ChatHistoryService {

    private ConcurrentHashMap<String, ConcurrentHashMap<String, List<Message>>> historyStorage = new ConcurrentHashMap<>();

    public void addMessage(Message message) {
        String from, to;
        String messageFrom = message.getFrom();
        String messageTo = message.getTo();
        if (messageFrom.compareTo(messageTo) > 0) {
            from = messageFrom;
            to = messageTo;
        } else {
            from = messageTo;
            to = messageFrom;
        }
        historyStorage.putIfAbsent(to, new ConcurrentHashMap<>());
        historyStorage.get(to).putIfAbsent(from, Collections.synchronizedList(new ArrayList<>()));
        List<Message> messageList = historyStorage.get(to).get(from);
        synchronized (messageList) {
            if (messageList.size() > 50) {
                messageList.remove(0);
            }
            messageList.add(message);
        }
    }

    public List<Message> getHistory(String sender, String recipient) {
        String from, to;
        if (sender.compareTo(recipient) < 0) {
            from = sender;
            to = recipient;
        } else {
            from = recipient;
            to = sender;
        }

        ConcurrentHashMap<String, List<Message>> map = historyStorage.get(from);
        if (nonNull(map)) {
            List<Message> messageList = map.get(to);
            if (nonNull(messageList) && messageList.size() > 0) {
                synchronized (messageList) {
                    return new LinkedList<>(messageList);
                }
            }
        }
        return Collections.emptyList();
    }
}
