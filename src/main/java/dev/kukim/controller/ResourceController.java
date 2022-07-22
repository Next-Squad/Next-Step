package dev.kukim.controller;

import dev.kukim.webserver.http.request.HttpRequest;
import dev.kukim.webserver.http.response.HttpResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ResourceController implements Controller {

	@Override
	public void process(HttpRequest request, HttpResponse response) throws IOException {
		response.setBody(Files.readAllBytes(new File(
			"./src/main/resources/webapp" + request.getPath()).toPath()));
		response.sendOK();
	}

}
