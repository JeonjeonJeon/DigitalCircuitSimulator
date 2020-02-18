package graphicComponent;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.io.Serializable;

import graphic.WorkSpace;

public class Line implements Serializable{
	private static final long serialVersionUID = -109243898914932421L;
	
	
	public double x1, y1;
	public double x2, y2;
	
	public Line(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public void draw(Graphics2D g) {
		g.draw(new Line2D.Double(
				x1*WorkSpace.ratio*WorkSpace.coordinateInterval - WorkSpace.offsetX, 
				y1*WorkSpace.ratio*WorkSpace.coordinateInterval - WorkSpace.offsetY, 
				x2*WorkSpace.ratio*WorkSpace.coordinateInterval - WorkSpace.offsetX, 
				y2*WorkSpace.ratio*WorkSpace.coordinateInterval - WorkSpace.offsetY
				));
	}
	public void fill(Graphics2D g) {
		g.fill(new Line2D.Double(
				x1*WorkSpace.ratio*WorkSpace.coordinateInterval - WorkSpace.offsetX, 
				y1*WorkSpace.ratio*WorkSpace.coordinateInterval - WorkSpace.offsetY, 
				x2*WorkSpace.ratio*WorkSpace.coordinateInterval - WorkSpace.offsetX, 
				y2*WorkSpace.ratio*WorkSpace.coordinateInterval - WorkSpace.offsetY
				));
	}
	
	public boolean contains(double targetx, double targety) {
		if(x1 == x2) {
			if(y1 < targety && targety < y2) return true;
		}
		
		else {
			if(targety == targetx*(y2-y1)/(x2-x1) + y1 - x1*(y2-y1)/(x2-x1)) return true;
		}
		return false;
	}
	
	public double getX1() {
		return x1;
	}
	public double getY1() {
		return y1;
	}
	public double getX2() {
		return x2;
	}
	public double getY2() {
		return y2;
	}
	public String toString() {
		return "(" + x1 + ", " + y1 + ", " + x2 + ", " + y2 + ")";
	}
	
	/*
	public boolean intersects(double tx, double ty, double w, double h) {
		return contains(tx, ty) | contains(tx + w, ty) | contains(tx, ty + h) | contains(tx + w, ty + h);
	}
	
	public boolean intersects(Rectangle r) {
		return contains(r.x, r.y)|contains(r.x+r.width, r.y)|contains(r.x, r.y+height)|contains(r.x+r.width, r.y+height);
	}
	*/
}
