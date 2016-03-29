package formbean;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class RegisterForm {
	private String email;
	private String firstname;
	private String lastname;
	private String password;
	private String button;

	public RegisterForm(HttpServletRequest request) {
		email = request.getParameter("email");
		firstname = request.getParameter("firstName");
		lastname = request.getParameter("lastName");
		password = request.getParameter("password");
		button = request.getParameter("button");
	}

	public String getEmail() {
		return email;
	}
	public String getFirstName() {
		return firstname;
	}
	public String getLastName() {
		return lastname;
	}
	public String getPassword() {
		return password;
	}
	public String getButton() {
		return button;
	}
	public boolean isPresent() {
		return button != null;
	}

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		if (email == null || email.length() == 0) {
			errors.add("Email address is required");
		}
		if (firstname == null || firstname.length() == 0) {
			errors.add("First name is required");
		}
		if (lastname == null || lastname.length() == 0) {
			errors.add("Last name is required");
		}
		if (password == null || password.length() == 0) {
			errors.add("Password is required");
		}
		if (button == null) {
			errors.add("Button is required");
		}

		if (errors.size() > 0) {
			return errors;
		}
		if (!button.equals("Register")) {
			errors.add("Invalid button");
		}
		return errors;
	}
}

