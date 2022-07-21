package dev.kukim.webserver.controller;

import dev.kukim.webserver.http.request.HttpRequest;
import dev.kukim.webserver.http.response.HttpResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FrontController {

	private static final FrontController instance = new FrontController();

	public static FrontController getInstance() {
		return instance;
	}

	public void process(HttpRequest request, HttpResponse response) throws IOException {
		response.setBody(Files.readAllBytes(new File(
			"./src/main/resources/webapp" + request.getRequestLine().getPath()).toPath()));

		response.sendOK();
	}
}
