package test;

import junit.framework.TestCase;
import main.Main;

public class Tests extends TestCase {
	
	public void testQuantityOfRank(){
		short q  = Main.rankOfNumber(1000);
		assertEquals(q, (short) 4);
		
		q  = Main.rankOfNumber(1345132132);
		assertEquals(q, (short) 10);
		
		q  = Main.rankOfNumber(1);
		assertEquals(q, (short) 1);
		
	}

}
