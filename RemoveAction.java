package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import databean.FavoriteBean;
import databean.UserBean;
import model.FavoriteDAO;
import model.Model;
import model.MyDAOException;
import model.UserDAO;

public class RemoveAction extends Action {
	private FavoriteDAO favoriteDAO;
	private UserDAO  userDAO;

	public RemoveAction(Model model) {
		favoriteDAO = model.getFavoriteDAO();
		userDAO  = model.getUserDAO();
	}

	@Override
	public String getName() { return "remove.do"; }

	@Override
	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors",errors);
		FavoriteBean[] favoriteList;

		UserBean user = (UserBean)request.getSession().getAttribute("user");

		String favoriteId = request.getParameter("favoriteId");
		try {
			favoriteDAO.deleteEntry(favoriteId);
			favoriteList = favoriteDAO.getUserFavorite(user.getUserId());
			request.setAttribute("favoriteList", favoriteList);
			request.setAttribute("userList",userDAO.getList());
		} catch (MyDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "manage.jsp";
	}
}
