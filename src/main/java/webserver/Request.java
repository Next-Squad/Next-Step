package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

		processHttpMethod(httpMethod, url, out);
	}

	private void processHttpMethod(String httpMethod, String url, OutputStream out) {
		if (httpMethod.equals("GET")) {
			response.makeResonse(url, out);
		}
	}


}