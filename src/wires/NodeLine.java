package wires;

import java.awt.Graphics2D;
import java.io.Serializable;

import graphicComponent.Calc;
import graphicComponent.Ellipse;
import graphicComponent.Line;


public class NodeLine implements Serializable{
	private static final long serialVersionUID = 6964415457461289283L;
	
	public Line line;
	public double[] xs; //x, y can be null depends on number of length of line.
	public double[] ys; //in case of length is 1 x, y will be null
	int innerPoints;
	
	Ellipse startPoint;
	Ellipse endPoint;
	
	public NodeLine(Line newline) {
		line = newline;
		setLine(line.x1, line.y1, line.x2, line.y2);
	}
	public NodeLine(double x1, double y1, double x2, double y2) {//pixel coordinate
		setLine(x1, y1, x2, y2);
	}
	public void setLine(double x1, double y1, double x2, double y2) {
		line = new Line(x1, y1, x2, y2);
		if(line.y1 == line.y2) { //horizontal line
			innerPoints = (int)(Calc.abs(line.x2 - line.x1)) - 1;
		}
		else { //vertical line
			innerPoints = (int)(Calc.abs(line.y2 - line.y1)) - 1;
		}
		startPoint = new Ellipse(line.x1-0.1, line.y1-0.1, 0.2, 0.2);
		endPoint = new Ellipse(line.x2-0.1, line.y2-0.1, 0.2, 0.2);
		
		if(innerPoints != 0) {
			xs = new double[innerPoints];
			ys = new double[innerPoints];
			
			for(int i=0 ; i<innerPoints ; i++) {
				xs[i] = Calc.min(line.x1, line.x2) + (i+1)*Calc.isZero(line.x2 - line.x1);
				ys[i] = Calc.min(line.y1, line.y2) + (i+1)*Calc.isZero(line.y2 - line.y1);
			}
		}
		else {
			xs = null;
			ys = null;
		}
	}
	
	public void paint(Graphics2D g) {
		line.draw(g);
		startPoint.fill(g);
		endPoint.fill(g);
	}
	public boolean contains(double coorx, double coory) {
		if(startPoint.centerX == coorx && startPoint.centerY == coory) return true;
		else if(endPoint.centerX == coorx && endPoint.centerY == coory) return true;
		else if(xs != null) {
			for(int i=0 ; i<innerPoints ; i++) {
				if(xs[i] == coorx && ys[i] == coory) {
					return true;
				}
			}
		}
		return false;
	}
	
	public String toString() {
		return "(" + startPoint.centerX + ", " + startPoint.centerY + ", " + endPoint.centerX + ", " + endPoint.centerY + ")";
	}
	/*
	private void fill(Graphics2D g, Ellipse2D.Double e) {
		g.fill(new Ellipse2D.Double(e.x-WorkSpace.offsetX, e.y-WorkSpace.offsetY, 4, 4));
	}
	*/
}