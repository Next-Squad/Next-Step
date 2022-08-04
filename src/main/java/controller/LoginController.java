package controller;

import db.UserDataBase;
import http.Cookie;
import http.request.HttpRequest;
import http.response.HttpResponse;
import java.io.IOException;
import java.util.Map;
import model.User;
import util.HttpRequestUtils;

public class LoginController extends AbstractController{

	private static final String TRUE = "true";
	private static final String LOGIN = "logined";
	private static final String USER_ID = "userId";
	private static final String PASSWORD = "password";

	@Override
	protected void doPost(HttpRequest request, HttpResponse response) throws IOException {
		String messageBody = request.getMessageBody();
		Map<String, String> parsedMessageBody = HttpRequestUtils.parseQueryString(messageBody);
		String userId = parsedMessageBody.get(USER_ID);
		String password = parsedMessageBody.get(PASSWORD);
		User savedUser = UserDataBase.findUserById(userId);
		if (UserDataBase.login(savedUser, userId, password)) {
			response.found("/index.html", new Cookie(LOGIN, TRUE));
			return;
		}
		response.found("/user/login_failed.html");
	}
}
