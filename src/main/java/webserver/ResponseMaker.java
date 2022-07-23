package webserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ResponseMaker {
    private final Map<String, String> mapper = new HashMap<>();

    public ResponseMaker() {
        mapper.put("/index.html", "./webapp/index.html");
    }

    public Optional<Response> getResponse(Request request) throws IOException {
        String path = mapper.get(request.getUri());
        if (Objects.isNull(path)) {
            return Optional.empty();
        }
        byte[] body = Files.readAllBytes(new File(path).toPath());
        return Optional.of(new Response(StatusCode.OK, body));
    }
}
