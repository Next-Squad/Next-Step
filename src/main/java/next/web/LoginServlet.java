package next.web;

import core.db.DataBase;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/user/login")
public class LoginServlet extends HttpServlet {

	private static final Logger log = LoggerFactory.getLogger(LoginServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher("/user/login.jsp");
		rd.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		String userId = req.getParameter("userId");
		String password = req.getParameter("password");

		User loginUser = DataBase.findUserById(userId);

		if (Objects.isNull(loginUser) || !loginUser.checkPassword(password)) {
			log.debug("login 실패");
			RequestDispatcher rd = req.getRequestDispatcher("/user/login_failed.html");
			rd.forward(req, resp);
		} else {
			log.debug("login user : {} 성공", loginUser.getUserId());
			HttpSession session = req.getSession();
			session.setAttribute("user", loginUser);
			resp.sendRedirect("/");
		}
	}
}
