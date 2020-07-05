package graphicComponent;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;

import framework.WorkSpace;

public class Ellipse implements Serializable{
	private static final long serialVersionUID = 1357878472848882181L;
	
	
	public double x;
	public double y;
	public double width;
	public double height;
	public double centerX;
	public double centerY;

	public Ellipse(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		centerX = x + width/2;
		centerY = y + height/2;
	}
	public Ellipse() {
		this(0, 0, 0, 0);
	}

	public void draw(Graphics2D g) {
		g.draw(new Ellipse2D.Double(
				x*WorkSpace.ratio*WorkSpace.coordinateInterval - WorkSpace.offsetX, 
				y*WorkSpace.ratio*WorkSpace.coordinateInterval - WorkSpace.offsetY, 
				width*WorkSpace.ratio*WorkSpace.coordinateInterval, 
				height*WorkSpace.ratio*WorkSpace.coordinateInterval
				));
	}
	public void fill(Graphics2D g) {
		g.fill(new Ellipse2D.Double(
				x*WorkSpace.ratio*WorkSpace.coordinateInterval - WorkSpace.offsetX, 
				y*WorkSpace.ratio*WorkSpace.coordinateInterval - WorkSpace.offsetY, 
				width*WorkSpace.ratio*WorkSpace.coordinateInterval, 
				height*WorkSpace.ratio*WorkSpace.coordinateInterval
				));
	}

}
