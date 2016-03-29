package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import databean.UserBean;
import formbean.RegisterForm;
import model.Model;
import model.UserDAO;

/**
 * @author Chuhui Zhang (chuhuiz@andrew.cmu.edu)
 * Course number: 08-672
 * Dec 5, 2015
 */
public class RegisterAction extends Action {
	private UserDAO userDAO;

	public RegisterAction(Model model) {
		userDAO = model.getUserDAO();
	}

	@Override
	public String getName() { return "register.do"; }

	@Override
	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		try {
			request.setAttribute("userList",userDAO.getList());
			RegisterForm form = new RegisterForm(request);
			if(!form.isPresent()) {
				return "register.jsp";
			}

			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				return "register.jsp";
			}

			UserBean user = userDAO.read(form.getEmail());

			if (user != null) {
				errors.add("This email has already been registered");
				return "register.jsp";
			} else {
				user = new UserBean();
				user.setEmail(form.getEmail());
				user.setFirstName(form.getFirstName());
				user.setLastName(form.getLastName());
				user.setPassword(form.getPassword());
				userDAO.create(user);
				user.setUserId(userDAO.searchId(form.getEmail()));

				HttpSession session = request.getSession();
				//UserBean newUser = userDAO.read(form.getEmail());
				session.setAttribute("user", user);

				return "manage.do";
			}
		} catch (Exception e) {
			errors.add(e.getMessage());
			return "register.jsp";
		}
	}
}
