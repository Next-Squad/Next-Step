package service;

import db.Session;
import model.User;

public class LoginService {

    public String login(User user) {
        return Session.addSession(user);
    }

    public void logout(String sessionId) {
        Session.deleteSession(sessionId);
    }
}
