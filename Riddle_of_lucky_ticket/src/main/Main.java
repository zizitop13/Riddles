package main;

public class Main {

	public static void main(String[] args) {
		
		short rank = 4;
		int number = 0;
		int[] buffer = {0,0,0,0};
		
		while(rankOfNumber(number+1) <= rank) {
			number++;
			for(int i=rank-rankOfNumber(number); i >= 0; i--) {

			}

		}
		 
//		int[] number = new int[n];
//		
//		for(int r = n-1; r >=0; r--)
//			for(int i = 0; i < 10; i++) {
//				number[r] = i;
//				System.out.println(arrayToString(number));
//				
//			}

	}
	
	public static short rankOfNumber(Integer n) {
		short q = 1;
		
		while(n/10>=1) {
			q++;
			n=n/10;
		}
		return q;
		
	}
	
	
	private static String arrayToString(int[] arr) {
		StringBuffer sb = new StringBuffer(arr.length);
		
		for(int i =0; i < arr.length; i++) 			
			sb.append(arr[i]);
		
		return sb.toString();
	}

}
