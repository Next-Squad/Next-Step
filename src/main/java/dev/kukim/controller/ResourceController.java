package dev.kukim.controller;

import dev.kukim.webserver.http.request.HttpRequest;
import dev.kukim.webserver.http.response.HttpResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ResourceController extends AbstractController {

	public static final String CLASSPATH_STATIC_RESOURCE = "./src/main/resources/static";


	@Override
	protected void doGet(HttpRequest request, HttpResponse response) throws IOException {
		setResource(request, response);
		response.sendOK();
	}

	private void setResource(HttpRequest request, HttpResponse response) throws IOException {
		String path = request.getPath();
		if (path.endsWith(".html")) {
			byte[] body = Files.readAllBytes(new File(
				CLASSPATH_STATIC_RESOURCE + request.getPath()).toPath());
			response.setBody(body);
			response.setContentType("text/html;charset=utf-8");
		} else if (path.endsWith(".css")) {
			byte[] body = Files.readAllBytes(new File(
				CLASSPATH_STATIC_RESOURCE + request.getPath()).toPath());
			response.setBody(body);
			response.setContentType("text/css;charset=utf-8");
		} else if (path.endsWith(".js")) {
			byte[] body = Files.readAllBytes(new File(
				CLASSPATH_STATIC_RESOURCE + request.getPath()).toPath());
			response.setBody(body);
			response.setContentType("text/js;charset=utf-8");
		} else if (path.endsWith("/favicon.ico")) {
			byte[] body = Files.readAllBytes(new File(
				CLASSPATH_STATIC_RESOURCE + request.getPath()).toPath());
			response.setBody(body);
			response.setContentType("image/x-icon;charset=utf-8");
		} else if (path.contains("/fonts")) {
			byte[] body = Files.readAllBytes(new File(
				CLASSPATH_STATIC_RESOURCE + request.getPath()).toPath());
			response.setBody(body);
			String fontExtension = getFontExtension(path);
			response.setContentType("application/x-font-" + fontExtension);
		} else {
			throw new IllegalArgumentException("Content Type을 지원하지 않습니다 : " + path);
		}
	}

	private String getFontExtension(String path) {
		return path.substring(path.lastIndexOf(".") + 1);
	}

}
