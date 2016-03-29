package model;

/**
 * @author Chuhui Zhang (chuhuiz@andrew.cmu.edu)
 * Course number: 08-672
 * Dec 5, 2015
 */
public class MyDAOException extends Exception {
	private static final long serialVersionUID = 1L;

	public MyDAOException(Exception e) { super(e); }
	public MyDAOException(String s)    { super(s); }

}

