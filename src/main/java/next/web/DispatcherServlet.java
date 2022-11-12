package next.web;

import next.di.AnnotationScanner;
import next.web.controller.Controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {



    @Override
    public void init() throws ServletException {
        AnnotationScanner annotationScanner = new AnnotationScanner("next.web");

        super.init();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getRequestURI());
//        Controller controller = requestMapping.getController(req.getRequestURI());
//
//        try {
//            String viewName = controller.execute(req, resp);
//            move(viewName, req, resp);
//        } catch (Exception e) {
//            throw new ServletException(e.getMessage());
//        }
    }

    private void move(String viewName, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(viewName);
        requestDispatcher.forward(req, resp);
    }

}
