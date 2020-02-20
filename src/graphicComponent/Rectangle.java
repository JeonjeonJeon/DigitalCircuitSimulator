package graphicComponent;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import graphic.WorkSpace;

public class Rectangle implements Serializable{
	private static final long serialVersionUID = 2600811281250071161L;
	
	
	public double x;
	public double y;
	public double width;
	public double height;
	
	
	public Rectangle(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	public Rectangle() {
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
	}
	public void setRect(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void draw(Graphics gg) {
		Graphics2D g = (Graphics2D)gg;
		g.draw(new Rectangle2D.Double(
				x*WorkSpace.ratio*WorkSpace.coordinateInterval - WorkSpace.offsetX, 
				y*WorkSpace.ratio*WorkSpace.coordinateInterval - WorkSpace.offsetY, 
				width*WorkSpace.ratio*WorkSpace.coordinateInterval, 
				height*WorkSpace.ratio*WorkSpace.coordinateInterval
				));
	}
	public void fill(Graphics2D g) {
		g.fill(new Rectangle2D.Double(
				x*WorkSpace.ratio*WorkSpace.coordinateInterval - WorkSpace.offsetX, 
				y*WorkSpace.ratio*WorkSpace.coordinateInterval - WorkSpace.offsetY, 
				width*WorkSpace.ratio*WorkSpace.coordinateInterval, 
				height*WorkSpace.ratio*WorkSpace.coordinateInterval
				));
	}
	
	public boolean contains(double targetx, double targety) {
		if(x <= targetx && targetx <= x + width) {
			if(y <= targety && targety <= y + height) {
				return true;
			}
		}
		return false;
	}
	
	
	public boolean intersects(double tx, double ty, double w, double h) {
		boolean case1;
		boolean case2;
		Rectangle target = new Rectangle(tx, ty, w, h);
		case1 = contains(tx, ty) | contains(tx + w, ty) | contains(tx, ty + h) | contains(tx + w, ty + h);
		case2 = target.contains(x, y) | target.contains(x+width, y) | target.contains(x, y+height) | target.contains(x+width, y+height);
		return case1 | case2;
	}
	
	
	public boolean intersects(Rectangle r) {
		boolean case1;
		boolean case2;
		case1 = contains(r.x, r.y)|contains(r.x+r.width, r.y)|contains(r.x, r.y+height)|contains(r.x+r.width, r.y+height);
		case2 = r.contains(x, y)|r.contains(x+width, y)|r.contains(x, y+height)|r.contains(x+width, y+height);
		return case1|case2;
	}
	
	public boolean intersects(Line l) { // vertical line |
		if(l.x1 == l.x2) {
			if(Calc.sgn(l.x1 - x) == Calc.sgn(l.x1 - (x + width))) { // does not intersect
				  return false;
			}
			else {
				if(y + height < Calc.min(l.y1, l.y2) || y > Calc.max(l.y2, l.y1)) {
					return false;
				}
			}
		}
		
		else if(l.y1 == l.y2) { // horizontal line -
			if(Calc.sgn(l.y1 - y) == Calc.sgn(l.y1 - (y + height))) {
				  return false;
			}
			else {
				if(x > Calc.max(l.x1, l.x2) || x + width < Calc.min(l.x1, l.x2)) {
					return false;
				}
			}
		}
		
		else {
			System.out.println("diogonal line detected at Rectangle.intersect(Line l)!!");
			return false;
		}
		return true;
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getWidth() {
		return width;
	}
	public double getHeight() {
		return height;
	}
	public boolean isEmpty() {
		if(getHeight() == 0 && getWidth() == 0) return true;
		return false;
	}
	
	public String toString() {
		return "(" + x + ", " + y + ", " + width + ", " + height + ")";
	}
}
