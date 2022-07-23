package service;

import db.DataBase;
import model.User;


public class UserService {

    public void saveUser(String userId, String password, String name, String email) {
       DataBase.findUserById(userId).ifPresent(user -> {
           throw new IllegalArgumentException("이미 있는 아이디 입니다.");
       });

       User user = new User(userId, password, name, email);
       DataBase.addUser(user);
    }
}
