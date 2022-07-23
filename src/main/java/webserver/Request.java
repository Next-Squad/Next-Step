package webserver;

import db.DataBase;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import util.IOUtils;
import webserver.domain.HttpMethod;
import webserver.domain.RequestHeader;
import webserver.domain.RequestLine;

public class Request {

	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	private RequestHeader requestHeader;
	private int contentLength;

	public RequestLine handleUserRequest(InputStream in) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

		makeRequestHeader(bufferedReader);
		processHttpMethod();

		return requestHeader.getRequestLine();
	}

	private void makeRequestHeader(BufferedReader bufferedReader)
		throws IOException {

		String line = bufferedReader.readLine();
		String startLine = line;
		log.debug("requestHeader: {}", line);

		List<String> requestData = new ArrayList<>();
		while (!"".equals(line)) {
			line = bufferedReader.readLine();
			log.debug("requestHeader: {}", line);

			if (line.contains("Content-Length")) {
				String[] token = line.split(":");
				contentLength = Integer.parseInt(token[1].trim());
			}

			if (line == null) {
				return;
			}
			requestData.add(line);
		}

		requestHeader.setBody(IOUtils.readData(bufferedReader, contentLength));


		this.requestHeader = new RequestHeader(startLine, requestData);
	}

	private void processHttpMethod() {
		HttpMethod httpMethod = requestHeader.getRequestLine().getHttpMethod();
		String url = requestHeader.getRequestLine().getUrl();

		if (httpMethod.equals(HttpMethod.GET)) {
			int indexOfQueryParameter = url.indexOf('?');
			if (indexOfQueryParameter != -1) {
				processQueryParameter(url, indexOfQueryParameter);
			}
		} else if (httpMethod.equals(HttpMethod.POST)) {
			Map<String, String> queryStrings = HttpRequestUtils.parseQueryString(requestHeader.getBody());
			saveUserInDatabase(queryStrings);
		}

	}

	private void processQueryParameter(String url, int indexOfQueryParameter) {
		String requestPath = url.substring(0, indexOfQueryParameter);
		String queryString = url.substring(indexOfQueryParameter + 1);

		Map<String, String> queryStringMap = HttpRequestUtils.parseQueryString(queryString);

		saveUserInDatabase(queryStringMap);
	}

	private void saveUserInDatabase(Map<String, String> queryStringMap) {
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
		DataBase.findAll().stream()
			.forEach(userInDatabase -> log.debug("**Database.findAll() : {}",
				userInDatabase.toString()));
		//TODO : index.html로 redirection 되도록 처리해야하지 않을까? 요구사항에 없으니 패스
	}

}