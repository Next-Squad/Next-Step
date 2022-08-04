package controller;

import db.UserDataBase;
import http.request.HttpRequest;
import http.response.HttpResponse;
import java.io.IOException;
import java.util.Map;
import model.User;
import util.HttpRequestUtils;

public class SignUpController extends AbstractController{

	private static final String USER_ID = "userId";
	private static final String PASSWORD = "password";
	private static final String NAME = "name";
	private static final String EMAIL = "email";

	@Override
	protected void doPost(HttpRequest request, HttpResponse response) throws IOException {
		String messageBody = request.getMessageBody();
		Map<String, String> parsedMessageBody = HttpRequestUtils.parseQueryString(messageBody);
		UserDataBase.addUser(
			new User(
				parsedMessageBody.get(USER_ID),
				parsedMessageBody.get(PASSWORD),
				parsedMessageBody.get(NAME),
				parsedMessageBody.get(EMAIL))
		);
		response.found("/index.html");
	}
}
