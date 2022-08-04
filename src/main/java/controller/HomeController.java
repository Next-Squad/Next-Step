package controller;

import db.URLDataBase;
import http.MimeType;
import http.request.HttpRequest;
import http.request.RequestLine;
import http.request.RequestURI;
import http.response.HttpResponse;
import java.io.IOException;

public class HomeController extends AbstractController{

	@Override
	protected void doGet(HttpRequest request, HttpResponse response) throws IOException {
		String url = request.getRequestURIPath();
		if (URLDataBase.contains(url) || MimeType.isSupportedExtension(url)) {
			response.ok(url);
			return;
		}
		response.ok();
	}
}
