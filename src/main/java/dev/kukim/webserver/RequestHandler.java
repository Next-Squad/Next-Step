package dev.kukim.webserver;

import dev.kukim.webserver.controller.FrontController;
import dev.kukim.webserver.http.request.HttpRequest;
import dev.kukim.webserver.http.response.HttpResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {

	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	private static final FrontController frontController = FrontController.getInstance();

	private final Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	@Override
	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
			connection.getPort());

		try (InputStream inputStream = connection.getInputStream();
			OutputStream outputStream = connection.getOutputStream()) {

			HttpRequest httpRequest = new HttpRequest(inputStream);
			HttpResponse httpResponse = new HttpResponse(outputStream);

			frontController.process(httpRequest, httpResponse);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
