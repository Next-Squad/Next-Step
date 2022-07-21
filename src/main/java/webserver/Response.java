package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Response {

	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	public void makeResonse(String url, OutputStream out) {
		DataOutputStream dos = new DataOutputStream(out);

		byte[] body = null;

		if (url.equals("/index.html")) {
			try {
				body = Files.readAllBytes(Path.of("webapp", url.replace("/", "")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			body = "Hello World".getBytes();
		}

		response200Header(dos, body.length);

		responseBody(dos, body);

	}

	private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void responseBody(DataOutputStream dos, byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
