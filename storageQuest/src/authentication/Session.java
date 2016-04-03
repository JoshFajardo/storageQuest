package authentication;

public class Session {
	public static int nextId = 1;
	
	
	private int sessionId;
	
	
	private User sessionUser;
	
	public Session(User user){
		sessionUser = user;
		sessionId= nextId++;
	}
	
	public User getSessionUser() {
		return sessionUser;
	}

	public void setSessionUser(User sessionUser) {
		this.sessionUser = sessionUser;
	}

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

}
