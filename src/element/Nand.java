package element;

import java.awt.Color;
import java.awt.Graphics2D;

import main.Voltage;
import wires.GateIO;

public class Nand extends Element {
	private static final long serialVersionUID = -7977837069261648184L;
	
	GateIO output;
	GateIO input1;
	GateIO input2;
	
	public Nand(double coorx, double coory) {
		super(coorx, coory);
		output = new GateIO(coordx + 5, coordy + 2, 1);
		input1 = new GateIO(coordx, coordy+1, 0);
		input2 = new GateIO(coordx, coordy+3, 0);
		data.addGateIO(output);
		data.addGateIO(input1);
		data.addGateIO(input2);
	}

	@Override
	public void paint(Graphics2D g) {
		super.paint(g);
		
		/*--draw AND gate*/
		g.setColor(Color.BLACK);
		drawLine(g, 0, 1, 0.5, 1); 
		drawLine(g, 0, 3, .5, 3); 
		drawLine(g, 4.5, 2, 5, 2); 
		drawLine(g, 0.5, 0.5, 2.5, 0.5); 
		drawLine(g, 0.5, 3.5, 2.5, 3.5); 
		drawLine(g, 0.5, 0.5, 0.5, 3.5);
		drawArc(g, 1, 0.5, 3, 3, -90, 180);
		drawArc(g, 4, 1.75, 0.5, 0.5, 0, 360);
		
		bound.setRect(coordx + 1, coordy, 3, 4);
		
		output.paint(g); 
		input1.paint(g); 
		input2.paint(g); 
	}

	@Override
	public void removeData() {
		data.removeGateIO(output);
		data.removeGateIO(input1);
		data.removeGateIO(input2);	
	}

	@Override
	public void move(double x, double y) {
		coordx = x - 2;
		coordy = y - 2;
		
		bound.setRect(coordx + 1, coordy, 3, 4);
		output.setPos(coordx + 5, coordy + 2);
		input1.setPos(coordx, coordy + 1);
		input2.setPos(coordx, coordy + 3);
		
		//do something with node
	}
	
	/*
	 * truth table
	 * A B Out
	 * 0 0 1 v
	 * 0 1 1 v
	 * 1 0 1 v
	 * 1 1 0 v
	 * 
	 * 1 x x v
	 * 0 x 1 v
	 * 1 z z v
	 * 0 z 1 v
	 * */
	public void sim1() {
		if(input1.getState() == Voltage.LOW || input2.getState() == Voltage.LOW) {
			internalState = Voltage.HIGH;
		}
		else if(input1.getState() == Voltage.HIGH && input2.getState() == Voltage.HIGH) {
			internalState = Voltage.LOW;
		}
		else if(input1.getState() == Voltage.HIZ || input2.getState() == Voltage.HIZ) {
			internalState = Voltage.HIZ;
		}
		else System.out.println("error in nand gate");
	}
	
	public void sim2(){
		output.setState(internalState);
	}
	
	public Element copy() {
		Nand a = new Nand(coordx, coordy);
		return a;
	}
	
	@Override
	public String toNetlist() {
		String r = ".";
		r = r + this.getClass() + "\n";

		r = r + "INPUT=";
		r = r + input1.getNode().name + ",";
		r = r + input2.getNode().name + "\n";

		r = r + "OUTPUT=";
		r = r + output.getNode().name + "\n";

		r = r + "POSITION=";
		r = r + coordx + ",";
		r = r + coordy;
		
		return r;
	}
}
