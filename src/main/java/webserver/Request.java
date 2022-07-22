package webserver;

import db.DataBase;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class Request {

	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	private Response response = new Response();

	public void parseRequest(InputStream in, OutputStream out) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

		processRequestLine(out, bufferedReader);
	}

	private void processRequestLine(OutputStream out, BufferedReader bufferedReader)
		throws IOException {

		String line = bufferedReader.readLine();
		log.debug("**{}", line);
		if (line == null) {
			return;
		}

		String[] requestLine = line.split(" ");
		String httpMethod = requestLine[0];
		String url = requestLine[1];
		String httpVersion = requestLine[2];

		if (httpMethod.equals("GET")) {
			int indexOfQueryParameter = url.indexOf('?');
			if (indexOfQueryParameter != -1) {
				processQueryParameter(url, indexOfQueryParameter);
			}

			response.makeResponse(url, out);
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