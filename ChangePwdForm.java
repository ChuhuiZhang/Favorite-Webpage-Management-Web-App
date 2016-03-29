package formbean;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Chuhui Zhang (chuhuiz@andrew.cmu.edu)
 * Course number: 08-672
 * Dec 5, 2015
 */
public class ChangePwdForm {
	private String confirmPassword;
	private String newPassword;
	private String button;

	public ChangePwdForm(HttpServletRequest request) {
		confirmPassword = request.getParameter("confirmPassword");
		newPassword = request.getParameter("newPassword");
		button = request.getParameter("button");

	}

	public String getConfirmPassword() {
		return confirmPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public String getButton() {
		return button;
	}
	public boolean isPresent() {
		return button != null;
	}


	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		if (newPassword == null || newPassword.length() == 0) {
			errors.add("New Password is required");
		}

		if (confirmPassword == null || confirmPassword.length() == 0) {
			errors.add("Confirm Pwd is required");
		}

		if (errors.size() > 0) {
			return errors;
		}

		if (!newPassword.equals(confirmPassword)) {
			errors.add("Passwords do not match");
		}

		return errors;
	}
}