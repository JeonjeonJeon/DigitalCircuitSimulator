package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;

import util.Calc;
import util.Line;
import util.Rectangle;
import window.WorkSpace;

public class Node implements Serializable{
	private static final long serialVersionUID = 6737281024992084310L;
	
	
	double coordx = 0;
	double coordy = 0;
	private int containing = -1;
	boolean selected = false;
	public int type = 0;//0: new node, 1: called node
	private int state = Voltage.LOW;
	
	ArrayList<NodeLine> lines = new ArrayList<NodeLine>();
	Line currentLine = null;
	
	//double RI = WorkSpace.ratio * WorkSpace.coordinateInterval;
	

	public Node(double x, double y) {
		coordx = x;
		coordy = y;
		currentLine = new Line(coordx, coordy, x, y);
	}
	
	public void mergeNode(Node nodeTemp) {//add info from nodeTemp to this
		//System.out.println("merge node");
		for(NodeLine nl : nodeTemp.lines) {
			lines.add(nl);
		}
		for(GateIO io : WorkSpace.ios) {
			if(io.getNode() != null && io.getNode().equals(nodeTemp)){
				io.setNode(this);
			}
		}
	}
	
	public Node callNode(double coorx, double coory) {
		//System.out.println("call node");
		type = 1;
		for(NodeLine nl : lines) {
			if(nl.xs != null) {
				for(int i=0 ; i<nl.xs.length ; i++) {
					if(nl.xs[i] == coorx && nl.ys[i] == coory) { //selected in the middle of node line
						NodeLine newline = new NodeLine(coorx, coory, nl.line.x2, nl.line.y2);
						lines.add(newline);
						nl.setLine(nl.line.x1, nl.line.y1, coorx, coory);
						currentLine = new Line(coorx, coory, coorx, coory);
						return this;
					}
				}
			}
		}
		currentLine = new Line(coorx, coory, coorx, coory);
		return this;
	}
	
	public void finishNode() {
		//System.out.println("finish node");
		lines.add(new NodeLine(currentLine));
		currentLine = null;
	}
	
	public void nextLine() {
		lines.add(new NodeLine(currentLine));
		currentLine = new Line(currentLine.x2, currentLine.y2, currentLine.x2, currentLine.y2);
	}
	
	public void paint(Graphics2D g, double mx, double my) {
		//if(containing != -1) g.setColor(Color.PINK);
		//else g.setColor(Color.BLACK);
		for(int i=0 ; i<lines.size() ; i++) {
			if(i == containing) {
				g.setColor(Color.RED);
			}
			else {
				g.setColor(Voltage.getColor(state));
			}
			if(selected == true) {
				g.setColor(Color.BLUE);
			}
			lines.get(i).paint(g);
		}
		
		//drawing current line, it varies by mouse cursor movement
		if(currentLine != null) {
			if(Calc.abs(currentLine.x1 - mx) > Calc.abs(currentLine.y1 - my)) { //logic not to make diagonal lines, horizontal line
				currentLine.x2 = mx;
				currentLine.y2 = currentLine.y1;
			}
			else { //vertical line
				currentLine.x2 = currentLine.x1;
				currentLine.y2 = my;
			}
			currentLine.draw(g);
		}
	}
	
	public boolean contains(double coorx, double coory) {
		for(int i = 0 ; i < lines.size() ; i++) {
			if(lines.get(i).contains(coorx, coory) == true) {
				containing = i;
				return true;
			}
		}
		containing = -1;
		return false;
	}

	public boolean select(Rectangle dragBox) {
		for(NodeLine l : lines) {
			if(dragBox.intersects(l.line) == true) {
				selected = true;
				return true;
			}
		}
		selected = false;
		return false;
	}
	
	public boolean startSim() {
		int sum = 0;
		int count = 0;
		for(GateIO io : WorkSpace.ios) {
			if(io.getNode().equals(this)) {
				if(io.getPriority() == 1) count++;
				sum += io.getPriority();
			}
		}
		if(sum >= 3 || count >= 2) {
			state = Voltage.ERROR;
			return false;
		}
		return true;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setState(int c) {
		state = c;
	}
	public int getState() {
		return state;
	}
}
