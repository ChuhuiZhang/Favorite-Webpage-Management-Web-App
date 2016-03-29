package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import databean.FavoriteBean;
import databean.UserBean;
import model.FavoriteDAO;
import model.Model;
import model.UserDAO;

/**
 * @author Chuhui Zhang (chuhuiz@andrew.cmu.edu)
 * Course number: 08-672
 * Dec 5, 2015
 */
public class ManageAction extends Action {
	private FavoriteDAO favoriteDAO;
	private UserDAO userDAO;

	public ManageAction(Model model) {
		favoriteDAO = model.getFavoriteDAO();
		userDAO = model.getUserDAO();
	}

	@Override
	public String getName() { return "manage.do"; }

	@Override
	public String perform(HttpServletRequest request) {
		// Set up the errors list
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors",errors);

		try {
			// Set up user list for nav bar
			request.setAttribute("userList",userDAO.getList());

			UserBean user = (UserBean) request.getSession(false).getAttribute("user");
			FavoriteBean[] favoriteList = favoriteDAO.getUserFavorite(user.getUserId());
			request.setAttribute("favoriteList", favoriteList);
			return "manage.jsp";
		} catch (Exception e) {
			errors.add(e.getMessage());
			return "error.jsp";
		}
	}

}
