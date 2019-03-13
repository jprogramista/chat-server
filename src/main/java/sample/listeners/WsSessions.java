package sample.listeners;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class WsSessions {

    private BiMap<String, String> map =  Maps.synchronizedBiMap(HashBiMap.create());

    public void put(String key, String value) {
        map.putIfAbsent(key, value);
    }

    public void remove(String key) {
        map.remove(key);
    }

    public String get(String key) {
        return map.get(key);
    }

    public String getByValue(String value) {
        return map.inverse().get(value);
    }
    
}
