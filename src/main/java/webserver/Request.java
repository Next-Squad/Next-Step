package webserver;

import db.DataBase;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.domain.HttpMethod;
import webserver.domain.RequestHeader;
import webserver.domain.RequestLine;

public class Request {

	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	public RequestLine handleUserRequest(InputStream in) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

		RequestLine requestLine = parseHeader(bufferedReader);
		processHttpMethod(requestLine);

		return requestLine;
	}

	private RequestLine parseHeader(BufferedReader bufferedReader)
		throws IOException {
		RequestHeader requestHeader = new RequestHeader();

		//1.RequestLine
		String line = bufferedReader.readLine();
		log.debug("requestHeader: {}", line);
		requestHeader.makeRequestLine(line);

		//2.RequestHeaders
		List<String> requestHeaders = new ArrayList<>();
		while (!"".equals(line)) {
			line = bufferedReader.readLine();
			log.debug("requestHeader: {}", line);

			if (!"".equals(line)) {
				requestHeaders.add(line);
			}

			if (line == null) {
				return requestHeader.getRequestLine();
			}
		}
		requestHeader.setRequestHeaders(requestHeaders);

//		line = bufferedReader.readLine();
//		List<String> body = new ArrayList<>();
//		while (!"".equals(line)) {
//			if (line == null) {
//				break;
//			}
//			body.add(line);
//			line = bufferedReader.readLine();
//		}
//		requestHeader.setBody(body);

		return requestHeader.getRequestLine();
	}

	private void processHttpMethod(RequestLine requestLine) {
		HttpMethod httpMethod = requestLine.getHttpMethod();
		String url = requestLine.getUrl();

		if (httpMethod.equals(HttpMethod.GET)) {
			int indexOfQueryParameter = url.indexOf('?');
			if (indexOfQueryParameter != -1) {
				processQueryParameter(url, indexOfQueryParameter);
			}
		} else if (httpMethod.equals(HttpMethod.POST)) {

		}

	}

	private void processQueryParameter(String url, int indexOfQueryParameter) {
		String requestPath = url.substring(0, indexOfQueryParameter);
		String queryString = url.substring(indexOfQueryParameter + 1);

		Map<String, String> queryStringMap = HttpRequestUtils.parseQueryString(queryString);

		User user = new User();
		for (Entry<String, String> entry : queryStringMap.entrySet()) {
			switch (entry.getKey()) {
				case "userId":
					user.setUserId(entry.getValue());
					break;
				case "name":
					user.setName(entry.getValue());
					break;
				case "password":
					user.setPassword(entry.getValue());
					break;
				case "email":
					user.setEmail(URLDecoder.decode(entry.getValue(), StandardCharsets.UTF_8));
					break;
			}
		}

		DataBase.addUser(user);
		//TODO : index.html로 redirection 되도록 처리해야하지 않을까? 요구사항에 없으니 패스
	}

}