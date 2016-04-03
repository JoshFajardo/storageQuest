package login;

import java.util.HashMap;

public class ABACPolicy {

	//function access for the warehouse
	
	public static final String CAN_VIEW_WAREHOUSE = "warehouse.view";
	public static final String CAN_ADD_WAREHOUSE= "warehouse.add";
	public static final String CAN_EDIT_WAREHOUSE = "warehouse.edit";
	public static final String CAN_DELETE_WAREHOUSE = "warehouse.delete";
	
	//function access for the parts
	
	public static final String CAN_VIEW_PART = "part.view";
	public static final String CAN_ADD_PART= "part.add";
	public static final String CAN_EDIT_PART = "part.edit";
	public static final String CAN_DELETE_PART = "part.delete";
	
	
	private HashMap<String, HashMap<String, Boolean>> acl;
	
	public ABACPolicy(){
		
		acl = new HashMap<String, HashMap<String, Boolean>>();
		
		createSimpleUserACLEntry("default",false,false,false,false,false);
	}
	
	
	//puts in the values for a particular user
	
	public void createSimpleUserACLEntry(String login, boolean ... entries){
		HashMap<String, Boolean>userTable = new HashMap<String, Boolean>();
		userTable.put(CAN_VIEW_WAREHOUSE, entries[0]);
		userTable.put(CAN_VIEW_PART, entries[1]);
		userTable.put(CAN_ADD_PART, entries[2]);
		userTable.put(CAN_EDIT_PART, entries[3]);
		userTable.put(CAN_DELETE_PART, entries[4]);
		
		acl.put(login, userTable);
	}
	
	public void setUserACLEntry(String uName, String f, boolean val)throws SecurityException{
	
		//checks for the user name in the ACL
		if(!acl.containsKey(uName))
			throw new SecurityException(uName + " does not exsit in ACL");
		HashMap<String, Boolean>userTable = acl.get(uName);
		//checks if the function is in the table
		if(!userTable.containsKey(f))
			throw new SecurityException(f + " does not exist in user table");
		
		userTable.put(f, val);
		 
	}
	
	public boolean canUserAccessFunction(String userName, String functionName){
		
		if(!acl.containsKey("default"))
			return false;
		HashMap<String, Boolean> userTable = acl.get("default");
		
		//gets user's table if it is in the acl
		if(acl.containsKey(userName))
			userTable = acl.get(userName);
		//is permission for function in table
		if(userTable.containsKey(functionName))
			return userTable.get(functionName);
		
		
		//permission does not exist so return false
		return false;
	}
	
	
	
	
}
