package element;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GateIO;
import main.Node;
import main.Voltage;
import window.WorkSpace;

public class Monitor extends Element {
	private static final long serialVersionUID = -8193793001401432150L;
	
	
	GateIO input;
	Color output = new Color(0, 0, 0);
	
	public Monitor(double coorx, double coory) {
		super(coorx, coory);
		input = new GateIO(coordx, coordy + 1, 0);
		WorkSpace.ios.add(input);
	}

	@Override
	public void paint(Graphics2D g) {
		super.paint(g);
		
		/*--draw--*/
		g.setColor(Color.BLACK);
		drawLine(g, 1, 0, 3, 0);
		drawLine(g, 1, 0, 1, 2);
		drawLine(g, 3, 0, 3, 2);
		drawLine(g, 1, 2, 3, 2);
		drawLine(g, 0, 1, 1, 1);
		//drawArc(g, 1.5, 0.5, 1, 1, 0, 360);
		g.setColor(output);
		fillArc(g, 1.5, 0.5, 1, 1, 0, 360);
		
		bound.setRect(coordx + 1, coordy, 3, 2);
		input.paint(g); 
	}

	@Override
	public void removeData() {
		WorkSpace.ios.remove(input);	
	}

	@Override
	public void move(double x, double y) {
		coordx = x - 2;
		coordy = y - 2;
		
		bound.setRect(coordx + 1, coordy, 3, 2);
		input.setPos(coordx, coordy + 1);
		
		//do something with node
	}
	
	public void sim1() {
		internalState = input.getState();
	}
	public void sim2() {
		output = Voltage.getColor(internalState);
	}
	public Node getLinkedNode() {
		return input.getNode();
	}
	
	public Element copy() {
		Monitor a = new Monitor(coordx, coordy);
		return a;
	}
}
