package dev.kukim.controller;

import dev.kukim.webserver.http.request.HttpRequest;
import dev.kukim.webserver.http.response.HttpResponse;
import java.io.IOException;

public abstract class AbstractController implements Controller{

	@Override
	public void service(HttpRequest request, HttpResponse response) throws IOException {
		switch (request.getMethod()) {
			case GET:
				this.doGet(request, response);
				return;
			case POST:
				this.doPost(request, response);
				return;
			default:
				response.responseMethodNotAllowed();
		}
	}

	protected void doGet(HttpRequest request, HttpResponse response) throws IOException {
		response.responseMethodNotAllowed();
	}

	protected void doPost(HttpRequest request, HttpResponse response) throws IOException {
		response.responseMethodNotAllowed();
	}
}
