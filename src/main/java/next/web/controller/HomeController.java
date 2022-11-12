package next.web.controller;

import next.web.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController{

    @RequestMapping(path = "/", requestMethod = RequestMethod.GET)
    public String showHome(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "home.jsp";
    }
}
