package login;

import db.DataBase;
import login.dto.LoginResult;
import model.User;

public class LoginService {

    public LoginResult login(String userId, String password) {
        User findUser = DataBase.findUserById(userId);
        return getLoginResult(password, findUser);
    }

    private LoginResult getLoginResult(String password, User findUser) {
        if (password.equals(findUser.getPassword())) {
            return success();
        }
        return fail();
    }

    private LoginResult fail() {
        return new LoginResult("/user/login_failed.html", false);
    }

    private LoginResult success() {
        return new LoginResult("/index.html", true);
    }



    // todo 싱글톤 메서드, 동시성 처리

}
