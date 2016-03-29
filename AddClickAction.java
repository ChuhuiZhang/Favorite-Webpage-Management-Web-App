package controller;

import javax.servlet.http.HttpServletRequest;

import databean.FavoriteBean;
import databean.UserBean;
import model.FavoriteDAO;
import model.Model;
import model.MyDAOException;
import model.UserDAO;

public class AddClickAction extends Action {
	private FavoriteDAO favoriteDAO;
	private UserDAO userDAO;

	public AddClickAction(Model model) {
		favoriteDAO = model.getFavoriteDAO();
		userDAO = model.getUserDAO();
	}

	@Override
	public String getName() {
		return "addClick.do";
	}

	@Override
	public String perform(HttpServletRequest request) {
		UserBean user = (UserBean)request.getSession(false).getAttribute("user");
		FavoriteBean[] favoriteList;
		try {
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
		return "manage.jsp";
	}
}
