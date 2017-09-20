package tests;

import junit.framework.TestCase;
import main.Methods;

public class Test extends TestCase {
	
	
	
	public void testGetNextArr(){
		//n=2 k =9 => 10
		short exp = 10;
		short k = 9;
		int[] arr = {1,1,1,1,1,1,1,1,1,1};
		
		assertEquals(Methods.getNextArr(arr)[k], exp);
		
	}

}
