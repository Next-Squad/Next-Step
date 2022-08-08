package controller;

import db.UserDataBase;
import http.request.HttpRequest;
import http.request.RequestHeaders;
import http.response.HttpResponse;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class UserController extends AbstractController{

	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	private static final String LOGIN = "logined";
	private static final String TRUE = "true";

	@Override
	protected void doGet(HttpRequest request, HttpResponse response) throws IOException {
		RequestHeaders requestHeaders = request.getRequestHeaders();
		if (!requestHeaders.hasCookie()){
			log.debug("RequestHeaders has not Cookie!");
			response.ok("/user/login.html");
			return;
		}

		String cookies = requestHeaders.getCookie();
		Map<String, String> parsedCookies = HttpRequestUtils.parseCookies(cookies);
		if (parsedCookies.get(LOGIN).equals(TRUE)) {
			response.ok("/user/list.html", UserDataBase.findAll());
			return;
		}
		response.ok("/user/login.html");
	}
}
