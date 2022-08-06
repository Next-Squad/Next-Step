package db;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.WebServer;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Session {
    private static final Logger log = LoggerFactory.getLogger(Session.class);

    private static final Map<String, User> sessionDb = new ConcurrentHashMap<>();

    public static String addSession(User user) {
        log.debug("add session {}",user.toString());
        String sessionId = UUID.randomUUID().toString();
        sessionDb.put(sessionId, user);
        return sessionId;
    }

    public static boolean checkSession(String sessionId) {
        return Optional.ofNullable(sessionDb.get(sessionId)).isPresent();
    }

    public static void deleteSession(String sessionId) {
        sessionDb.remove(sessionId);
    }


}
