package db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.User;

public class UserDataBase {

    private static final Map<String, User> users = new HashMap<>();

    private UserDataBase() {}

    public static void addUser(User user) {
        if (checkExistingUserId(user.getUserId())) {
            throw new IllegalArgumentException("중복된 userId 입니다.");
        }
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        if (checkExistingUserId(userId)) {
            return users.get(userId);
        }
        return null;
    }

    public static List<User> findAll() {
        List<User> userList = new ArrayList<>();
        users.forEach((k, v) -> userList.add(v));
        return userList;
    }

    private static boolean checkExistingUserId(String userId) {
        return users.containsKey(userId);
    }

    public static boolean login(User user, String userId, String password) { // login 로직이 여기 있는게 맞을까?
        if (user == null) {
            return false;
        }
        return userId.equals(user.getUserId()) && password.equals(user.getPassword());
    }
}
