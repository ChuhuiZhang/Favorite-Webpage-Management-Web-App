package databean;

public class UserBean {
	private int userId;
	private String email;
	private String password;
	private String firstname;
	private String lastname;

	public int getUserId() {
		return userId;
	}
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	public String getFirstName() {
		return firstname;
	}
	public String getLastName() {
		return lastname;
	}


	public void setUserId(int s) {
		userId = s;
	}
	public void setEmail(String s) {
		email = s;
	}
	public void setPassword(String s) {
		password = s;
	}
	public void setFirstName(String s) {
		firstname = s;
	}
	public void setLastName(String s) {
		lastname = s;
	}
}
