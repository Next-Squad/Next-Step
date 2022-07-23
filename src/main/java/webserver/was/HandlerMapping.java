package webserver.was;

import handler.Handler;
import webserver.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HandlerMapping {

     private final Map<RequestMappingInfo, Handler> mappingInfo = new HashMap<>();

    public HandlerMapping(Map<RequestMappingInfo, Handler> mappingInfo) {
        this.mappingInfo.putAll(mappingInfo);
    }

    public Map<RequestMappingInfo, Handler> getMappingInfo() {
        return Map.copyOf(mappingInfo);
    }

    public Optional<Handler> findHandler(HttpMethod method, String path) {
        return Optional.ofNullable(mappingInfo.get(new RequestMappingInfo(method, path)));
    }
}
