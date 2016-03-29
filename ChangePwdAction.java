package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import databean.UserBean;
import formbean.ChangePwdForm;
import model.Model;
import model.UserDAO;


public class ChangePwdAction extends Action {
	private UserDAO userDAO;

	public ChangePwdAction(Model model) {
		userDAO = model.getUserDAO();
	}
	@Override
	public String getName() {
		return "change-pwd.do";
	}

	@Override
	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors",errors);
		UserBean user = (UserBean)request.getSession(false).getAttribute("user");

		try {
			request.setAttribute("userList",userDAO.getList());
			ChangePwdForm form = new ChangePwdForm(request);

			if (!form.isPresent()) {
				return "change-pwd.jsp";
			}

			// Check for any validation errors
			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				return "change-pwd.jsp";
			}
			userDAO.changePassword(user.getUserId(),form.getNewPassword());
			HttpSession session = request.getSession(false);
			session.setAttribute("user",null);

		} catch (Exception e) {
			errors.add(e.toString());
			return "error.jsp";
		}
		return "login.jsp";
	}

}
