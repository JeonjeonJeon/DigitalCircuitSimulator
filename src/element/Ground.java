package element;

import java.awt.Color;
import java.awt.Graphics2D;

import main.Voltage;
import wires.GateIO;

public class Ground extends Element{
	private static final long serialVersionUID = 6218953185657470482L;
	
	
	GateIO output;
	
	public Ground(double coorx, double coory) {
		super(coorx, coory);
		output = new GateIO(coordx + 1, coordy, 2);
		data.addGateIO(output);
	}

	@Override
	public void paint(Graphics2D g) {
		super.paint(g);
		
		/*--draw AND gate*/
		g.setColor(Color.BLACK);
		drawLine(g, 1, 0, 1, 0.8);
		drawLine(g, 0.2, 0.8, 1.8, 0.8);
		drawLine(g, 0.2, 0.8, 1, 1.6);
		drawLine(g, 1.8, 0.8, 1, 1.6);
		
		bound.setRect(coordx, coordy + 1, 2, 1);
		
		output.paint(g); 
	}
	
	public void sim1() {
		internalState = Voltage.LOW;
	}
	public void sim2() {
		output.setState(internalState);
	}

	@Override
	public void removeData() {
		data.removeGateIO(output);	
	}

	@Override
	public void move(double x, double y) {
		coordx = x - 2;
		coordy = y - 2;
		
		bound.setRect(coordx, coordy + 1, 2, 1);
		output.setPos(coordx + 1, coordy);
		
		//do something with node
	}
	
	public Element copy() {
		Ground a = new Ground(coordx, coordy);
		return a;
	}
}
