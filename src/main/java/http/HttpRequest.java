package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
            String[] s = line.split(" ");
            method = s[0];
            String[] split1 = s[1].split("[?]");
            path = split1[0];
            parameter = HttpRequestUtils.parseQueryString(split1[1]);

            if (line == null) {
                return;
            }
            line = br.readLine();
            log.debug("라인: {}", line);
            while (!line.equals("")) {
                String[] split = line.split(":");
                header.put(split[0].trim(), split[1].trim());
                line = br.readLine();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
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
