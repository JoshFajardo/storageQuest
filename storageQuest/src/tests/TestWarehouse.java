package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import models.Warehouse;

/*
 * Test # 	Test case description				Expected result
 * 1		If warehouse is null				false
 * 2		if warehouse is > 255				false
 * 3		if warehouse is blank				false
 * 4		if warehouse has special chars		true
 * 5		if warehouse name normal			true
 * 6		if address is null					false
 * 7		if address is empty					false
 * 8		if address is > 255 				false
 * 9		if address is normal				true
 * 10		if address has special chars		true
 * 11		if state is large >50				false
 * 12		if state is null					false
 * 13		if state is normal					true
 * 14		if zip has letters					false
 * 15		if zip is too large > 5				false
 * 
 * 
 * 
 * 
 */

public class TestWarehouse {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	private static final String VALID_WAREHOUSE_NAME = "Cabinet Warehouse";
	private static final String VALID_ADDRESS = "101 UTSA Circle";
	private static final String VALID_STATE = "Texas";
	private static final String VALID_CITY = "San Antonio";
	private static final String VALID_ZIP  = "78228";
	private static final String VALID_STORAGE_CAPACITY = "9,001";
	
	private static Warehouse testWarehouse;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		testWarehouse = new Warehouse();
	}
	
	@Before
	public void setUp() throws Exception{
		
	}
	//tests for Warehouse name
	@Test
	public void testWarehouseNameInvalidNull(){
		String testWN = null;
		assertEquals(false,testWarehouse.validWarehouseName(testWN));
	}
	@Test //2
	public void testWarehouseNameTooLarge(){
		String testWN = "";
		for(int i = 0; i< 256; i++){
			testWN +="z";
		}
		assertEquals(false,testWarehouse.validWarehouseName(testWN));
	}
	@Test //3
	public void testWarehouseNameEmpty(){
		String testWN = "";
		assertEquals(false,testWarehouse.validWarehouseName(testWN));
	}
	@Test //4
	public void testWarehouseNameSpecialChar(){
		String testWN = "warehouse$fosizzle!";
		assertEquals(true,testWarehouse.validWarehouseName(testWN));
	}
	@Test //5
	public void testWarehouseNameValid(){
		assertEquals(true,testWarehouse.validWarehouseName(VALID_WAREHOUSE_NAME));
	}
	@Test //6
	public void testAddressNameNull(){
		String testAD = null;
		assertEquals(false,testWarehouse.validAddress(testAD));
	}
	@Test //7
	public void testAddressEmpty(){
		String testAD = "";
		assertEquals(false,testWarehouse.validAddress(testAD));
	}
	@Test //8
	public void testAddressTooLarge(){
		String testAD = "";
		for(int i = 0; i<256; i++){
			testAD +="z";
		}
		assertEquals(false,testWarehouse.validAddress(testAD));
	}
	@Test //9
	public void testAddressNormal(){
		assertEquals(true,testWarehouse.validAddress(VALID_ADDRESS));
		
	}
	@Test //10
	public void testAddressSpecialCharacters(){
		String testAD = "wh1t3h0u53L4n3!@";
		assertEquals(true,testWarehouse.validAddress(testAD));
	}
	@Test //11
	public void testStateLarge(){
		String testST = "";
		for(int i = 0; i<51; i++){
			testST += "z";
		}
		assertEquals(false,testWarehouse.validState(testST));
	}
	@Test //12
	public void testStateNull(){
		String testST = null;
		assertEquals(false,testWarehouse.validState(testST));
	}
	@Test //13
	public void testStateNormal(){
		assertEquals(true,testWarehouse.validState(VALID_STATE));
	}
	@Test //14
	public void testZipHasLetters(){
		String testZp = "zip";
		assertEquals(false,testWarehouse.validZip(testZp));
	}
	@Test //15
	public void testZipLarge(){
		String testZp = "";
		for(int i = 0; i<6; i++){
			testZp += "0";
		}
		assertEquals(false,testWarehouse.validZip(testZp));
	}
}

	

