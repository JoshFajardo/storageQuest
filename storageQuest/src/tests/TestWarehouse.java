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
 * 6		
 * 7
 * 8
 * 9
 * 10
 * 11
 * 12
 * 13
 * 14
 * 15
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
}

	

