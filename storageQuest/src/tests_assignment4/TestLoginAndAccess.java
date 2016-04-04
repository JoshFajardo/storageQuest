package tests_assignment4;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import login.User;
import login.Verify;
import login.ABACPolicy;
import login.SecurityException;

/*Test #	Test case description		Expected
 * 1		
 * 
 * 
 *
 */



public class TestLoginAndAccess {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	private static final String VALID_LOGIN ="bob";
	private static final String VALID_PASSWORD ="123456";
	private static final String VALID_USERNAME ="boberino";
	
	private static User testUser;
	private static User testInvalidUser;
	
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		testUser = new User(VALID_LOGIN,VALID_PASSWORD,VALID_USERNAME);
		testInvalidUser = new User("zeratul","shadurus", "zer");
		
	}
	
	@Before
	public void setUp() throws Exception{
		
	}
	
	@Test
	public void testBobPartAdd() {
		ABACPolicy pol = new ABACPolicy();

		testUser = new User(VALID_LOGIN,VALID_PASSWORD,VALID_USERNAME);
		assertEquals(true, pol.canUserAccessFunction(VALID_LOGIN, "part.add"));
		
	}
	@Test//2
	public void testBobPartView() {
		ABACPolicy pol = new ABACPolicy();

		testUser = new User(VALID_LOGIN,VALID_PASSWORD,VALID_USERNAME);
		assertEquals(true, pol.canUserAccessFunction(VALID_LOGIN, "part.view"));
		
	}
	@Test//3
	public void testBobPartDelete() {
		ABACPolicy pol = new ABACPolicy();

		testUser = new User(VALID_LOGIN,VALID_PASSWORD,VALID_USERNAME);
		assertEquals(false, pol.canUserAccessFunction(VALID_LOGIN, "part.delete"));
		
	}
	@Test//4
	public void testInvalidPartView() {
		ABACPolicy pol = new ABACPolicy();

		testUser = new User("zeratul","shadurus", "zer");
		assertEquals(false, pol.canUserAccessFunction("zeratul", "part.view"));
		
	}
}
