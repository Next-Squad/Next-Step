package service;

import db.DataBase;
import model.User;

import java.util.NoSuchElementException;
import java.util.Optional;


public class UserService {

    public void saveUser(String userId, String password, String name, String email) {
        DataBase.findUserById(userId).ifPresent(user -> {
            throw new IllegalArgumentException("이미 있는 아이디 입니다.");
        });

        User user = new User(userId, password, name, email);
        DataBase.addUser(user);
    }

    public boolean matchUser(String userId, String password) {
        Optional<User> optionalUser = DataBase.findUserById(userId);

        return optionalUser.isPresent() && optionalUser.get().isSamePassword(password);
    }

    public User findUser(String userId) {
       return DataBase.findUserById(userId).orElseThrow(NoSuchElementException::new);
    }
}
