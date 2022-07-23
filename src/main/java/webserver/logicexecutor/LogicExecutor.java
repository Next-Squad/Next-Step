package webserver.logicexecutor;

import webserver.request.Request;
import webserver.response.Response;

public interface LogicExecutor {
    Response run(Request request);
}
