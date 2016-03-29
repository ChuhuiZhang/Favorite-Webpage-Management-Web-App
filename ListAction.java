package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import databean.FavoriteBean;
import databean.UserBean;
import model.FavoriteDAO;
import model.Model;
import model.UserDAO;

public class ListAction extends Action {

	private FavoriteDAO favoriteDAO;
	private UserDAO  userDAO;

	public ListAction(Model model) {
		favoriteDAO = model.getFavoriteDAO();
		userDAO  = model.getUserDAO();

	}

	@Override
	public String getName() { return "list.do"; }

	@Override
	public String perform(HttpServletRequest request) {
		// Set up the request attributes (the errors list and the form bean so
		// we can just return to the jsp with the form if the request isn't correct)
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors",errors);

		try {
			// Set up user list for nav bar
			UserBean[] userList = userDAO.getList();

			request.setAttribute("userList", userList);

			String userE = request.getParameter("userE");
			if (userE == null || userE.length() == 0) {
				errors.add("User email must be specified");
				return "error.jsp";
			}

			// Set up favorite list
			UserBean user = userDAO.read(userE);
			if (user == null) {
				errors.add("Invalid User: "+ userE);
				return "error.jsp";
			}

			FavoriteBean[] favoriteList = favoriteDAO.getUserFavorite(user.getUserId());
			request.setAttribute("favoriteList",favoriteList);
			return "list.jsp";
		} catch (Exception e) {
			errors.add(e.getMessage());
			return "error.jsp";
		}
	}
}
