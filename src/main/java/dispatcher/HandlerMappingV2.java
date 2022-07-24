package dispatcher;

import java.util.HashMap;
import java.util.Map;

public class HandlerMappingV2 {

    private final Map<RequestMappingInfo, HandlerMethod> map;

    public HandlerMappingV2() {
        this.map = new HashMap<>();
    }

    public void addMappingInfo(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) {
        map.put(requestMappingInfo, handlerMethod);
    }

    public HandlerMethod getHandler(RequestMappingInfo requestMappingInfo) {
       return map.get(requestMappingInfo);
    }


}
