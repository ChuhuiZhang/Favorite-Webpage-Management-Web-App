package controller;

import javax.servlet.http.HttpServletRequest;

import databean.FavoriteBean;
import databean.UserBean;
import model.FavoriteDAO;
import model.Model;
import model.MyDAOException;
import model.UserDAO;

public class VisitorClickAction extends Action {
	private FavoriteDAO favoriteDAO;
	private UserDAO userDAO;

	public VisitorClickAction(Model model) {
		favoriteDAO = model.getFavoriteDAO();
		userDAO = model.getUserDAO();
	}

	@Override
	public String getName() {
		return "visitorClick.do";
	}

	@Override
	public String perform(HttpServletRequest request) {
		FavoriteBean[] favoriteList;

		try {
			UserBean user = userDAO.read(request.getParameter("userE"));
			favoriteDAO.clickCount(Integer.parseInt(request.getParameter("link")));
			favoriteList = favoriteDAO.getUserFavorite(user.getUserId());
			request.setAttribute("favoriteList", favoriteList);
			request.setAttribute("userList",userDAO.getList());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MyDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "list.jsp";
	}

}
