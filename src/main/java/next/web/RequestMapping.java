package next.web;

import next.web.controller.Controller;
import next.web.controller.HomeController;
import next.web.controller.ListUserController;

import java.util.HashMap;
import java.util.Map;


public class RequestMapping {

    private final Map<String, Controller> mappings;

    public RequestMapping(Map<String, Controller> mappings) {
        this.mappings = mappings;
    }

    private void initMapping() {
        this.mappings.put("/user/list", new ListUserController());
        this.mappings.put("/" , new HomeController());
    }

    public Controller getController(String uri) {
        return mappings.get(uri);
    }

}
