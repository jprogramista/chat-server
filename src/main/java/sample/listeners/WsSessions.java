package sample.listeners;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class WsSessions {

    private ConcurrentMap<String, String> map = new ConcurrentHashMap<>();

    public void put(String key, String value) {
        map.putIfAbsent(key, value);
    }

    public void remove(String key) {
        map.remove(key);
    }

    public String get(String key) {
        return map.get(key);
    }

}
