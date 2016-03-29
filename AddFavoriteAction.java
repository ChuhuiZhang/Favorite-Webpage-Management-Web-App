package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import databean.FavoriteBean;
import databean.UserBean;
import formbean.ManageForm;
import model.FavoriteDAO;
import model.Model;
import model.MyDAOException;
import model.UserDAO;

public class AddFavoriteAction extends Action{
	private FavoriteDAO favoriteDAO;
	private UserDAO userDAO;

	public AddFavoriteAction(Model model) {
		favoriteDAO = model.getFavoriteDAO();
		userDAO = model.getUserDAO();
	}

	@Override
	public String getName() {
		return "addfavorite.do";
	}

	@Override
	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		ManageForm form = new ManageForm(request);
		request.setAttribute("errors", errors);
		UserBean user = (UserBean)request.getSession(false).getAttribute("user");
		FavoriteBean[] favoriteList;


		errors.addAll(form.getValidationErrors());
		if (errors.size() > 0) {
			try {
				request.setAttribute("userList",userDAO.getList());
				favoriteList = favoriteDAO.getUserFavorite(user.getUserId());
				request.setAttribute("favoriteList", favoriteList);
			} catch (MyDAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("I am here");
			return "manage.jsp";
		}

		try {

			FavoriteBean favorite = new FavoriteBean();
			favorite.setUserId(user.getUserId());
			favorite.setURL(form.getURL());
			favorite.setComment(form.getComment());
			favorite.setCount(0);
			favoriteDAO.create(favorite);
			favorite.setfavoriteId(favoriteDAO.searchFavoriteId(favorite.getURL()));

			favoriteList = favoriteDAO.getUserFavorite(user.getUserId());
			request.setAttribute("favoriteList", favoriteList);
			request.setAttribute("userList",userDAO.getList());
		} catch (Exception e) {
			errors.add(e.getMessage());
			return "error.jsp";
		}

		return "manage.jsp";

	}


}
