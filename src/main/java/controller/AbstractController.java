package controller;

import http.request.HttpRequest;
import http.request.RequestLine;
import http.response.HttpResponse;
import java.io.IOException;

public abstract class AbstractController implements Controller{

	@Override
	public void service(HttpRequest request, HttpResponse response) throws IOException {
		RequestLine requestLine = request.getRequestLine();
		if (requestLine.getHttpMethod().isGET()) {
			doGet(request, response);
		}

		if (requestLine.getHttpMethod().isPOST()) {
			doPost(request, response);
		}
	}

	protected void doGet(HttpRequest request, HttpResponse response) throws IOException {}

	protected void doPost(HttpRequest request, HttpResponse response) throws IOException {}
}
