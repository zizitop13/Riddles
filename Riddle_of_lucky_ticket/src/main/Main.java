package main;

public class Main {
	
	

	public static void main(String[] args) {
		
		int n = 10000;
		int qantity = 0; 
		int[] arr = {1,1,1,1,1,1,1,1,1,1};
		
		for(int i = 0; i < n/2-1; i++) 			
			arr = Methods.getNextArr(arr);		
		
		for(int j : arr)
			qantity += Math.pow(j, 2);


		
		System.out.println(qantity);
	}
	

}
