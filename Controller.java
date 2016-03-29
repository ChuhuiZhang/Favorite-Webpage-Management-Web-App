package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import databean.UserBean;
import model.Model;
import model.MyDAOException;


@SuppressWarnings("serial")
public class Controller extends HttpServlet {

	@Override
	public void init() throws ServletException {
		String jdbcDriverName = getInitParameter("jdbcDriverName");
		String jdbcURL = getInitParameter("jdbcURL");
		Model model;

		try {
			model = new Model(jdbcDriverName, jdbcURL);

			Action.add(new ChangePwdAction(model));
			Action.add(new ListAction(model));
			Action.add(new LoginAction(model));
			Action.add(new LogoutAction(model));
			Action.add(new ManageAction(model));
			Action.add(new RegisterAction(model));
			Action.add(new RemoveAction(model));
			Action.add(new AddFavoriteAction(model));
			Action.add(new AddClickAction(model));
			Action.add(new VisitorClickAction(model));
		} catch (MyDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String nextPage = performTheAction(request);
		sendToNextPage(nextPage, request, response);
	}

	private String performTheAction(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String servletPath = request.getServletPath();
		UserBean user = (UserBean) session.getAttribute("user");
		String action = getActionName(servletPath);


		//System.out.println("servletPath="+servletPath+" requestURI="+request.getRequestURI()+"  user="+user);

		if (action.equals("register.do") || action.equals("login.do")) {
			// Allow these actions without logging in
			return Action.perform(action, request);
		}
		if (action.equals("list.do") || action.equals("visitorClick.do")) {
			return Action.perform(action, request);
		}

		if (user == null) {
			// If the user hasn't logged in, direct him to the login page
			return Action.perform("login.do", request);
		}

		// Let the logged in user run his chosen action
		return Action.perform(action, request);
	}


	private void sendToNextPage(String nextPage, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		if (nextPage == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND,
					request.getServletPath());
			return;
		}

		if (nextPage.endsWith(".do")) {
			response.sendRedirect(nextPage);
			return;
		}

		if (nextPage.endsWith(".jsp")) {
			RequestDispatcher d = request.getRequestDispatcher("WEB-INF/"
					+ nextPage);

			d.forward(request, response);
			return;
		}

		throw new ServletException(Controller.class.getName()
				+ ".sendToNextPage(\"" + nextPage + "\"): invalid extension.");
	}

	private String getActionName(String path) {
		// We're guaranteed that the path will start with a slash
		int slash = path.lastIndexOf('/');
		return path.substring(slash + 1);
	}
}
