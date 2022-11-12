package next.web.controller;

import core.db.DataBase;
import next.web.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ListUserController{

    @RequestMapping(path = "/users", requestMethod = RequestMethod.GET)
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("users", DataBase.findAll());
        return "/user/list.jsp";
    }
}
