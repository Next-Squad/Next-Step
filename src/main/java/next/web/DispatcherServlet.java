package next.web;

import next.di.BeanFactory;
import org.reflections.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private BeanFactory beanFactory;

    @Override
    public void init() throws ServletException {
        this.beanFactory = new BeanFactory("next.web");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        System.out.println(requestURI);
        RequestMethod requestMethod = RequestMethod.valueOf(req.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        HandlerMapping handlerMapping = beanFactory.getHandlerMapping();

        HandlerMethod handlerMethod = handlerMapping.getHandlerMethod(handlerKey);

        System.out.println("   " + handlerMethod.toString());
        try {

            String viewName = handlerMethod.handle(req, resp);
            move(viewName, req, resp);
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
    }

    private void move(String viewName, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(viewName);
        requestDispatcher.forward(req, resp);
    }

}
