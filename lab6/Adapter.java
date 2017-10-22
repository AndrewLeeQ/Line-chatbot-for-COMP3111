package skeleton;

import java.util.HashMap;

public class Adapter {
	public static final String[] BEVERAGES = new String[] {
			"Caff� Americano", "Caff� Mocha", "Caff� Latte", 
			"Cappuccino", "Caramel Macchiato", "Espresso" }; // You can change these

	/**
	* This function compute the edit distance between a string and every 
	* strings in your selected beverage set. The beverage with the minimum 
	* edit distance <= 3 is returned. The use of Wagner_Fischer algorithm
	* is shown in the main function in WagnerFischer.java
	**/
	public String getBeverage(String s){
		// TODO: find the word with minimum edit distance
		String answer = "";
		int best = -1;
		for(String beverage: BEVERAGES) {
			WagnerFischer wg = new WagnerFischer(s, beverage);
			int current = wg.getDistance();
			if(current <= 3 && (best == -1 || current < best)) {
				best = current;
				answer = beverage;
			}		
		}
		return answer;
	}
}
