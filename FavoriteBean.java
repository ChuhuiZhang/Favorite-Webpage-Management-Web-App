package databean;


public class FavoriteBean {
	private int favoriteId;
	private int userId;
	private String url;
	private String comment;
	private int count;

	public int getfavoriteId(){
		return favoriteId;
	}
	public int getUserId() {
		return userId;
	}
	public String getURL() {
		return url;
	}
	public String getComment() {
		return comment;
	}
	public int getCount() {
		return count;
	}

	public void setfavoriteId(int s){
		favoriteId = s;
	}

	public void setUserId(int s) {
		userId = s;
	}
	public void setURL(String s) {
		url = s;
	}
	public void setComment(String s) {
		comment = s;
	}
	public void setCount(int s) {
		count = s;
	}
}
