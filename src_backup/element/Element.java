package element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.io.Serializable;

import datahandler.DataHandle;
import framework.WorkSpace;
import graphicComponent.Rectangle;
import main.Voltage;

public abstract class Element implements Serializable{
	private static final long serialVersionUID = -2357157647420824090L;
	
	DataHandle data;
	
	double coordx = 0; // position of logic gate
	double coordy = 0;
	public boolean containing = false;
	boolean selected = false;
	Rectangle bound; // rectangular boundary of logic gate
	int internalState = Voltage.HIZ;
	
	
	public Element(double coorx, double coory) {
		coordx = coorx;
		coordy = coory;
		bound = new Rectangle();
		data = DataHandle.getInstance();
	}

	public void paint(Graphics2D g) {
		
		
		g.setColor(Color.BLACK);
		
		if(containing) {
			g.setColor(Color.red);
			bound.draw(g);
		}
		if(selected) {
			g.setColor(Color.blue);
			bound.draw(g);
		}
		
	}
	
	public boolean select(Rectangle dd) {
		if(bound.intersects(dd)) {
			selected = true;
			return true;
		}
		selected = false;
		return false;
	}
	
	public boolean contains(double coorMX, double coorMY) {
		if(bound.contains(coorMX, coorMY)) containing = true;
		else containing = false;
		return containing;
	}

	public boolean isSelected() {
		return selected;
	}

	public abstract void removeData();
	public abstract void move(double x, double y);
	public abstract void sim1();
	public abstract void sim2();
	public abstract Element copy();
	

	public double getX() {
		return coordx;
	}
	
	public double getY() {
		return coordy;
	}
	
	
	void drawLine(Graphics2D gg, double x1, double y1, double x2, double y2) { // drawing tool
		gg.draw(new Line2D.Double(
				coordx*WorkSpace.ratio*WorkSpace.coordinateInterval + x1*WorkSpace.ratio*WorkSpace.coordinateInterval - WorkSpace.offsetX, 
				coordy*WorkSpace.ratio*WorkSpace.coordinateInterval + y1*WorkSpace.ratio*WorkSpace.coordinateInterval - WorkSpace.offsetY, 
				coordx*WorkSpace.ratio*WorkSpace.coordinateInterval + x2*WorkSpace.ratio*WorkSpace.coordinateInterval - WorkSpace.offsetX, 
				coordy*WorkSpace.ratio*WorkSpace.coordinateInterval + y2*WorkSpace.ratio*WorkSpace.coordinateInterval - WorkSpace.offsetY
				));
	}
	void drawArc(Graphics2D gg, double x, double y, double width, double height, double sa, double ea) { // drawing tool
		gg.draw(new Arc2D.Double(
				coordx*WorkSpace.ratio*WorkSpace.coordinateInterval + x*WorkSpace.ratio*WorkSpace.coordinateInterval - WorkSpace.offsetX, 
				coordy*WorkSpace.ratio*WorkSpace.coordinateInterval + y*WorkSpace.ratio*WorkSpace.coordinateInterval - WorkSpace.offsetY, 
				width*WorkSpace.ratio*WorkSpace.coordinateInterval, 
				height*WorkSpace.ratio*WorkSpace.coordinateInterval, 
				sa, ea, Arc2D.OPEN));
	}
	void fillArc(Graphics2D gg, double x, double y, double width, double height, double sa, double ea) { // drawing tool
		gg.fill(new Arc2D.Double(
				coordx*WorkSpace.ratio*WorkSpace.coordinateInterval + x*WorkSpace.ratio*WorkSpace.coordinateInterval - WorkSpace.offsetX, 
				coordy*WorkSpace.ratio*WorkSpace.coordinateInterval + y*WorkSpace.ratio*WorkSpace.coordinateInterval - WorkSpace.offsetY, 
				width*WorkSpace.ratio*WorkSpace.coordinateInterval, 
				height*WorkSpace.ratio*WorkSpace.coordinateInterval, 
				sa, ea, Arc2D.OPEN));
	}
	void drawString(Graphics2D gg, double x, double y, String s) {
		gg.drawString(s, 
				(int) (coordx*WorkSpace.ratio*WorkSpace.coordinateInterval + x*WorkSpace.ratio*WorkSpace.coordinateInterval - WorkSpace.offsetX), 
				(int) (coordy*WorkSpace.ratio*WorkSpace.coordinateInterval + y*WorkSpace.ratio*WorkSpace.coordinateInterval - WorkSpace.offsetY));
				
	}
	/*
	public void draw(Graphics2D g, Rectangle2D.Double r) {
		g.draw(new Rectangle2D.Double(r.x-WorkSpace.offsetX, r.y-WorkSpace.offsetY, r.width, r.height));
	}
	*/
}
