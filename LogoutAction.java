package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.Model;
import model.MyDAOException;
import model.UserDAO;

/**
 * @author Chuhui Zhang (chuhuiz@andrew.cmu.edu)
 * Course number: 08-672
 * Dec 5, 2015
 */
public class LogoutAction extends Action {
	private UserDAO userDAO;

	public LogoutAction(Model model) {
		userDAO = model.getUserDAO();
	}

	@Override
	public String getName() { return "logout.do"; }

	@Override
	public String perform(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		session.setAttribute("user",null);
		try {
			request.setAttribute("userList",userDAO.getList());
		} catch (MyDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "login.jsp";
	}
}
