package graphicComponent;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import framework.WorkSpace;

public class Calc {
	
	
	public static double abs(double a) {
		if(a < 0) return -a;
		else if(a > 0) return a;
		else return 0;
	}
	
	public static double min(double a, double b) {
		if(a < b) return a;
		else if(a > b) return b;
		else return a;
	}
	
	public static double max(double a, double b) {
		if(a < b) return b;
		else if(a > b) return a;
		else return a;
	}
	public static int max(int a, int b) {
		if(a < b) return b;
		else if(a > b) return a;
		else return a;
	}
	
	public static double isZero(double a) {
		if(a == 0) return 0;
		else return 1;
	}
	
	public static double sgn(double a) {
		if(a > 0) return 1;
		else if(a < 0) return -1;
		else return 0;
	}
	
	public static double stickX(double d) { //get x coordinate from mouse position
		double returning = (d + WorkSpace.offsetX)/(WorkSpace.ratio*WorkSpace.coordinateInterval);
		returning = Math.round(returning);
		return returning;
	}
	
	public static double stickY(double d) { //get y coordinate from mouse position
		double returning = (d + WorkSpace.offsetY)/(WorkSpace.ratio*WorkSpace.coordinateInterval);
		returning = Math.round(returning);
		return returning;
	}
	
	public static void translate(Graphics2D g, Line2D.Double l) {
		double ox = WorkSpace.offsetX;
		double oy = WorkSpace.offsetY;
		g.draw(new Line2D.Double(l.x1-ox, l.y1-oy, l.x2-ox, l.y2-oy));
	}
}
