package login;

import java.util.ArrayList;
import java.util.List;


public class Verify {

	private ABACPolicy accessPolicy;
	
	private List<Session> sessions;
	
	private List<User> credentials;
	
	public Verify(){
		
		User u = new User("bob", "123456", "Boberino");
		u.setPasswordHash(Hasher.hashSha256(u.getPassword()));
		credentials = new ArrayList<User>();
		credentials.add(u);
		
		//initializes the session list
		sessions = new ArrayList<Session>();
		
		//makes an ABAC policy and adds some permissions to the user(bob)
		accessPolicy = new ABACPolicy();
		//bob can view both the warehouse and part,as well as add and edit a part, but not delete one
		accessPolicy.createSimpleUserACLEntry(u.getLogin(),true,true,true,true,false);
		
	}
	/**
	 * look up in ABAC Policy table for user.login and the f (function)
	 * 
	 * @return true or false
	 * 
	 */
	
	public boolean clientStateHasAccess(Session s, String f){
		return accessPolicy.canUserAccessFunction(s.getSessionUser().getLogin(),f );
	}
	
	
	/**
	 * user server state to determine if the session user has access to a particular 
	 * function
	 * @param sessionId
	 * @param f
	 * @return
	 * @throws SecurityException
	 */
	public boolean serverStateHasAccess(int sessionId, String f) throws SecurityException{
		for(Session s : sessions){
			if(s.getSessionId() == sessionId){
				
				return accessPolicy.canUserAccessFunction(s.getSessionUser().getLogin(), f);
			}
		}
		throw new SecurityException("Invalid session");
	}
	/**
	 * login and creates a new session, and checks if the credentials match the password 
	 * @param l
	 * @param pw
	 * @return
	 * @throws SecurityException
	 */
	public int login(String l, String pw) throws SecurityException{
		
		for(User u : credentials){
			if(u.getLogin().equals(l) && u.getPasswordHash().equals(pw)){
				//creates a session for the user, that isn't server side
				Session s = new Session(u);
				sessions.add(s);
				return s.getSessionId();
			}
		}
		throw new SecurityException("Authentication failed");
	}
	
	/**
	 * same as login, but uses the sha-256 of users password
	 * @param l
	 * @param pwHash
	 * @return
	 * @throws SecurityException
	 */
	public int loginSha256(String l, String pwHash)throws SecurityException{
		for(User u : credentials) {
			if(u.getLogin().equals(l) && u.getPasswordHash().equals(pwHash)) {
				
				Session s = new Session(u);
				sessions.add(s);
				return s.getSessionId();
			}
		}
		throw new SecurityException("Authentication failed");
	
	}
	/**
	 * removes the session at the specific index sessionId
	 * @param sessionId
	 */
	public void logout(int sessionId) {
		for(int i = sessions.size() - 1; i >= 0; i--) {
			Session s = sessions.get(i);
			if(s.getSessionId() == sessionId) {
				sessions.remove(i);
			}
		}
	}
	
}
