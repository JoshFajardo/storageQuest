package login;



public class User {
	private String login;
	private String password;
	private String userName;
	
	//the hash of the users password
	private String passwordHash;
	
	public User(String l, String p, String uN) {
		login = l;
		password = p;
		userName = uN;
		passwordHash = Hasher.hashSha256(p);
	}
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

}
