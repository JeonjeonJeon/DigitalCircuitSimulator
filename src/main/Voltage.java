package main;

import java.awt.Color;

public class Voltage {

	public static int LOW = 0;
	public static int HIGH = 1;
	public static int HIZ = 2; // high impedance
	public static int ERROR = 5;
	public static int RISING = 3;
	public static int FALLING = 4;
	
	public static Color getColor(int c) {
		if(c == LOW) return Color.BLACK;
		else if(c == HIGH) return Color.ORANGE;
		else if(c == ERROR) return Color.MAGENTA;
		else if(c == HIZ) return new Color(127, 0, 255); // violet
		else if(c == RISING) return Color.BLACK;
		else if(c == FALLING) return Color.BLACK;
		
		System.out.println("invalid state input main.Voltage.getColor");
		return Color.BLACK;
	}
}
