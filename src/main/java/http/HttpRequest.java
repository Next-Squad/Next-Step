package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private String method;
    private String path;
    private Map<String,String> header = new HashMap<>();
    private Map<String,String> parameter = new HashMap<>();

    public HttpRequest(InputStream in) {
        try(in){
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            parseRequestLine(line);


            if (line == null) {
                return;
            }

            line = br.readLine();
            while (!line.equals("")) {
                log.debug("헤더: {}", line);
                String[] split = line.split(":");
                header.put(split[0].trim(), split[1].trim());
                line = br.readLine();
            }

            if (method.equals("POST")){
                String body = URLDecoder.decode(IOUtils.readData(br, Integer.parseInt(header.get("Content-Length"))), StandardCharsets.UTF_8);
                parameter = HttpRequestUtils.parseQueryString(body);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void parseRequestLine(String line){
        log.debug("라인: {}", line);
        String[] parsedLines = line.split(" ");
        method = parsedLines[0];
        if (method.equals("POST")){
            path = parsedLines[1];
            return;
        }

        int index = parsedLines[1].indexOf("?");
        if (index == -1) {
            path = parsedLines[1];
        } else {
            path = parsedLines[1].substring(0, index);
            parameter = HttpRequestUtils.parseQueryString(parsedLines[1].substring(index+1));
        }
//        log.debug("유저아이디: {}, 비밀번호: {}, 유저이름: {}", parameter.get("userId"), parameter.get("password"), parameter.get("name"));
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHeader(String s) {
        return header.get(s);
    }

    public String getParameter(String s) {
        return parameter.get(s);
    }
}
