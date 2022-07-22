package dev.kukim.controller;

import dev.kukim.db.DataBase;
import dev.kukim.model.User;
import dev.kukim.webserver.http.request.HttpRequest;
import dev.kukim.webserver.http.request.domain.HttpMethod;
import dev.kukim.webserver.http.response.HttpResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserCreateController implements Controller{

	private static final UserCreateController instance = new UserCreateController();
	private static final Logger log = LoggerFactory.getLogger(UserCreateController.class);

	public static Controller getInstance() {
		return instance;
	}

	public UserCreateController() {
	}

	@Override
	public void process(HttpRequest request, HttpResponse response) throws IOException {
		if (request.getMethod().equals(HttpMethod.GET)) {
			doGet(request, response);
		} else if (request.getMethod().equals(HttpMethod.POST)) {
			doPost(request, response);
		}
	}

	private void doGet(HttpRequest request, HttpResponse response) throws IOException {
		User user = new User(request.getQueryParameter("userId"),
			request.getQueryParameter("password"),
			request.getQueryParameter("name"),
			request.getQueryParameter("email"));
		DataBase.addUser(user);

		log.info("Create User : {}", user);

		response.sendRedirect("/index.html");
	}

	private void doPost(HttpRequest request, HttpResponse response) throws IOException {
		User user = new User(request.getBodyQueryParameter("userId"),
			request.getBodyQueryParameter("password"),
			request.getBodyQueryParameter("name"),
			request.getBodyQueryParameter("email"));
		DataBase.addUser(user);

		log.info("Create User : {}", user);

		response.sendRedirect("/index.html");

	}
}
