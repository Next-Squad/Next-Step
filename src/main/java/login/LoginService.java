package login;

import db.DataBase;
import login.dto.LoginResult;
import model.User;

public class LoginService {

    private static LoginService loginService;

    private LoginService() {
    }

    public static LoginService getInstance() {
        if (loginService == null) {
            loginService = new LoginService();
        }
        return loginService;
    }

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
}
