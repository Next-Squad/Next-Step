package dev.kukim.webserver.controller;

import dev.kukim.controller.Controller;
import dev.kukim.controller.ResourceController;
import dev.kukim.controller.UserCreateController;
import dev.kukim.controller.UserLoginController;
import dev.kukim.webserver.http.request.HttpRequest;
import dev.kukim.webserver.http.response.HttpResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FrontController {

	private static final ResourceController resourceController = new ResourceController();
	private static final FrontController instance = new FrontController();

	public static FrontController getInstance() {
		return instance;
	}

	private final Map<String, Controller> controllerMap = new ConcurrentHashMap<>();

	private FrontController() {
		controllerMap.put("/user/create", UserCreateController.getInstance());
		controllerMap.put("/user/login", UserLoginController.getInstance());
	}

	public void service(HttpRequest request, HttpResponse response) throws IOException {
		Controller controller = getController(request);
		controller.process(request, response);
	}

	private Controller getController(HttpRequest request) {
		Controller controller = controllerMap.get(request.getPath());
		if (controller != null) {
			return controller;
		}
		return resourceController;
	}

}
