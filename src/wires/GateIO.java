package wires;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

import framework.WorkSpace;
import graphicComponent.Rectangle;

public class GateIO implements Serializable{
	private static final long serialVersionUID = 1257751337468471814L;
	
	
	double coordx;
	double coordy;
	boolean containing = false;
	Rectangle bound;
	Node linked = null;
	private int priority;//0 priority means input io, 1 1priority means output, 2 priority means source output
	
	public GateIO(double x, double y, int p) {
		coordx = x;
		coordy = y;
		priority = p;
		//bound = new Rectangle(x-RI/80, y-RI/80, RI/40, RI/40);
		bound = new Rectangle(x-WorkSpace.ratio*WorkSpace.coordinateInterval/80, y-WorkSpace.ratio*WorkSpace.coordinateInterval/80, WorkSpace.ratio*WorkSpace.coordinateInterval/40, WorkSpace.ratio*WorkSpace.coordinateInterval/40);
	}
	
	
	public void paint(Graphics2D g) {
		if(containing) {
			g.setColor(Color.MAGENTA);
			bound.draw(g);
		}
		if(linked == null) {
			g.setColor(Color.CYAN);
			bound.draw(g);
		}
	}
	
	public boolean contains(double coorx, double coory) {
		if(bound.contains(coorx, coory)) {
			containing = true;
			return true;
		}
		else {
			containing = false;
			return false;
		}
	}
	public void setPos(double x, double y) {
		coordx = x;
		coordy = y;
		bound.setRect(x-WorkSpace.ratio*WorkSpace.coordinateInterval/80, y-WorkSpace.ratio*WorkSpace.coordinateInterval/80, WorkSpace.ratio*WorkSpace.coordinateInterval/40, WorkSpace.ratio*WorkSpace.coordinateInterval/40);
	}
	public void setNode(Node n) {
		linked = n;
	}
	public Node getNode() {
		return linked;
	}
	public boolean isLinked() {
		if(linked == null) return false;
		else return true;
	}
	public double getX() {
		return coordx;
	}
	public double getY() {
		return coordy;
	}
	public int getState() {
		if(linked == null) return Voltage.HIZ;
		return linked.getState();
	}
	public boolean setState(int c) {
		if(linked == null) return false;
		linked.setState(c);
		return true;
		/*
		if(linked == null) return false;
		if(c == Voltage.ERROR) {
			linked.setState(Voltage.ERROR);
			return false;
		}
		if(linked.getPriority() > priority) {
			linked.setState(Voltage.ERROR);
			return false;
		}
		linked.setState(c);
		return true;
		*/
	}
	public int getPriority() {
		return priority;
	}
	
	@Override
	public String toString() {
		if(linked == null) return getClass() + " / linked: " + "null";
		else return getClass() + " / linked: " + linked.toString();
	}
}
