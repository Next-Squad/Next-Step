package webserver.response;

import webserver.logicexecutor.LogicExecutor;
import webserver.logicexecutor.LoginLogicExecutor;
import webserver.request.Request;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class ResponseMaker {
    private final Map<String, LogicExecutor> mapper = new HashMap<>();

    public ResponseMaker() {
        mapper.put("/user/create", new LoginLogicExecutor());
    }

    public Response getResponse(Request request) {
        if (mapper.containsKey(request.getUri())) {
            LogicExecutor logicExecutor = mapper.get(request.getUri());
            return logicExecutor.run(request);
        }
        try {
            byte[] body = Files.readAllBytes(new File("./webapp" + request.getUri()).toPath());
            return new Response(StatusCode.OK, body);
        } catch (IOException e) {
            return new Response(StatusCode.OK, "헬로우 월드!".getBytes(StandardCharsets.UTF_8));
        }
    }
}
