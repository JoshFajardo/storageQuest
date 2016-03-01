package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import models.Part;



/*Test #	Test case description			Expected
 * 1		part name is null				false
 * 2		part name too large				false
 * 3		part name left blank			false
 * 4		part name valid is valid		true
 * 5		part number is null				false
 * 6		part number is too large		false
 * 7		part number is blank			false
 * 8		part number is valid			true
 * 9		vendor is null					false
 * 10		vendor is blank					true
 * 11		vendor name is too large		false
 * 12		vendor name is valid			true
 * 13		vendor part num is null			false
 * 14		vendor part num is too large	false
 * 15		vendor part num is Valid		true
 * 
 *  
 */

public class TestPart {
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	private static final String VALID_PART_NAME = "Cabinet Warehouse";
	private static final String VALID_PART_NUM = "101 UTSA Circle";
	private static final String VALID_VENDOR = "Texas";
	private static final String VALID_VENDOR_PART_NUM = "San Antonio";
	private static final String VALID_UNIT_QUANTITY  = "78228";
	
	private static Part testPart;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		testPart = new Part();
	}
	
	@Before
	public void setUp() throws Exception{
		
	}
	
	@Test
	public void testPartNameInvalidNull(){
		String testPN = null;
		assertEquals(false,testPart.validPartName(testPN));
	}
	
	@Test//2
	public void testPartName2Large(){
		String testPN = "";
		for(int i = 0; i<256; i++){
			testPN += "z";
		}
		assertEquals(false,testPart.validPartName(testPN));
	}
	
	
	@Test//3
	public void testPartNameBlank(){
		String testPN = "";
		assertEquals(false, testPart.validPartName(testPN));
	}
	
	@Test //4
	public void testPartNameValidIsValid(){
		assertEquals(true,testPart.validPartName(VALID_PART_NAME));
	}
	
	@Test//5
	public void testPartNumIsNull(){
		String testPN = null;
		assertEquals(false,testPart.validPartNumber(testPN));
	}
	
	@Test //6
	public void testPartNum2Large(){
		String testPN = "";
		for(int i = 0; i<30; i++){
			testPN += "z";			
		}
		assertEquals(false,testPart.validPartNumber(testPN));
	}
	
	@Test //7
	public void testPartNumBlank(){
		String testPN = "";
		assertEquals(false,testPart.validPartNumber(testPN));
	}
	
	@Test //8
	public void testPartNumValidIsValid(){
		assertEquals(true,testPart.validPartNumber(VALID_PART_NUM));	
	}
	
	@Test //9
	public void testVendorNull(){
		String testV = null;
		assertEquals(false,testPart.validVendor(testV));
	}
	
	@Test // 10
	public void testVendorBlank(){
		String testV = "";
		assertEquals(true,testPart.validVendor(testV));
	}
	
	@Test //11
	public void testVendor2Large(){
		String testV = "";
		for(int i = 0; i<256; i++){
			testV += "z";
		}
		assertEquals(false,testPart.validVendor(testV));
	}
	
	@Test //12
	public void testVendorValid(){
		assertEquals(true,testPart.validVendor(VALID_VENDOR));
	}
	
	@Test //13
	public void testVendorPNisNull(){
		String testVPN = null;
		assertEquals(false,testPart.validVendorPartNumber(testVPN));
	}
	
	@Test //14
	public void testVendorPN2Large(){
		String testVPN = "";
		for(int i = 0; i<256; i++){
			testVPN += "z";
			
		}
		assertEquals(false,testPart.validVendorPartNumber(testVPN));
	}
	
	@Test //15
	public void testVendorPNValid(){
		assertEquals(true,testPart.validVendorPartNumber(VALID_VENDOR_PART_NUM));
	}

}
