package dev.kukim.controller;

import dev.kukim.webserver.http.request.HttpRequest;
import dev.kukim.webserver.http.response.HttpResponse;
import java.io.IOException;

public interface Controller {

	void process(HttpRequest request, HttpResponse response) throws IOException;

}
