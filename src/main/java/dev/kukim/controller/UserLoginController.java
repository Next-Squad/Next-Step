package dev.kukim.controller;

import dev.kukim.db.DataBase;
import dev.kukim.webserver.http.request.HttpRequest;
import dev.kukim.webserver.http.request.domain.HttpMethod;
import dev.kukim.webserver.http.response.HttpResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserLoginController implements Controller{

	private static final UserLoginController instance = new UserLoginController();
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);

	public static Controller getInstance() {
		return instance;
	}

	@Override
	public void process(HttpRequest request, HttpResponse response) throws IOException {
		if (request.getMethod().equals(HttpMethod.POST)) {
			doPost(request, response);
		}

	}

	private void doPost(HttpRequest request, HttpResponse response) throws IOException {
		String requestUserId = request.getBodyQueryParameter("userId");
		String requestPassword = request.getBodyQueryParameter("password");

		if (islogin(requestUserId, requestPassword)) {
			log.info("Login 성공 - userId : {}", requestUserId);
			response.setCookie("logined=true");
			response.sendRedirect("/index.html");
		}

		log.info("Login 실패 - userId : {}", requestUserId);
		response.setCookie("logined=false");
		response.sendRedirect("/user/login_failed.html");
	}

	private boolean islogin(String userId, String password) {
		return DataBase.findUserById(userId)
			.map(user -> user.checkPassword(password))
			.orElse(false);
	}
}
