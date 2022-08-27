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
import next.model.User;
import next.web.exception.NotFoundUserException;

@WebServlet("/user/update")
public class UpdateUserServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		String userId = req.getParameter("userId");
		User user = DataBase.findUserById(userId);

		req.setAttribute("user", user);
		RequestDispatcher rd = req.getRequestDispatcher("/user/update.jsp");
		rd.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		String updateUserId = req.getParameter("userId");

		User findUser = DataBase.findUserById(updateUserId);

		if (Objects.isNull(findUser)) {
			throw new NotFoundUserException();
		}

		// 권한 확인

		User updateUser = new User(updateUserId,
			req.getParameter("password"),
			req.getParameter("name"),
			req.getParameter("email"));

		findUser.update(updateUser);
		resp.sendRedirect("/");
	}
}
