package main;

public class Methods {
	
	
	
	
	/*
	 * функция для построения следующего массива из предыдущего
	 */
	public static int[] getNextArr(int[] prevArr) {
		
		
		int[] nextArr = new int[prevArr.length + 9];

		
		for(int i=0; i < nextArr.length; i++) {
			int q = 0;
			for(int j=0; j < 10; j++)  
				try {
					q+=prevArr[i-j];
				}catch (ArrayIndexOutOfBoundsException e) {
					q+=0;
				}
				nextArr[i] = q;

			

			
		}
		
		return nextArr;
		
	}

}
