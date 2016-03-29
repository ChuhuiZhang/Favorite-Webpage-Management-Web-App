package model;

/**
 * @author Chuhui Zhang (chuhuiz@andrew.cmu.edu)
 * Course number: 08-672
 * Dec 5, 2015
 */
public class Model {
	private FavoriteDAO favoriteDAO;
	private UserDAO  userDAO;

	private String jdbcDriver;
	private String jdbcURL;

	public Model(String jdbcDriver, String jdbcURL)
			throws MyDAOException {
		this.jdbcDriver = jdbcDriver;
		this.jdbcURL = jdbcURL;

		favoriteDAO = new FavoriteDAO(this.jdbcDriver, this.jdbcURL);
		userDAO = new UserDAO(this.jdbcDriver, this.jdbcURL);
	}
	//
	//	public Model(ServletConfig config) throws ServletException {
	//	}

	public FavoriteDAO getFavoriteDAO() {
		return favoriteDAO;
	}

	public UserDAO  getUserDAO()  {
		return userDAO;
	}
}
