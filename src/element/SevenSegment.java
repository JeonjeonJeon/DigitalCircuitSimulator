package element;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GateIO;
import main.Voltage;

public class SevenSegment extends Element{

	private static final long serialVersionUID = 3211704455559602993L;

	GateIO input[];
	Color output[];
	
	Color o = Color.BLACK;
	Color b = Color.WHITE;
	
	public SevenSegment(double coorx, double coory) {
		super(coorx, coory);
		input = new GateIO[4];
		output = new Color[7];
		for(int i = 0; i < 4; i++) {
			input[i]= new GateIO(coordx, coordy + 1 + i, 0);
			data.addGateIO(input[i]);
		}
		for(int i = 0; i < 7; i++) {
			output[i] = new Color(0, 0, 0);
		}
		
	}

	@Override
	public void paint(Graphics2D g) {
		super.paint(g);
		
		/*--draw--*/
		g.setColor(Color.BLACK);
		drawLine(g, 1, 0, 4, 0); // upper
		drawLine(g, 1, 0, 1, 5); // left
		drawLine(g, 4, 0, 4, 5); // right
		drawLine(g, 1, 5, 4, 5); // lower
		
		drawLine(g, 0, 1, 1, 1);
		drawLine(g, 0, 2, 1, 2);
		drawLine(g, 0, 3, 1, 3);
		drawLine(g, 0, 4, 1, 4);
		
		g.setColor(output[0]);
		drawLine(g, 1.5, 0.5, 3.5, 0.5); // a
		g.setColor(output[1]);
		drawLine(g, 3.5, 0.5, 3.5, 2.5); // b
		g.setColor(output[2]);
		drawLine(g, 3.5, 2.5, 3.5, 4.5); // c
		g.setColor(output[3]);
		drawLine(g, 1.5, 4.5, 3.5, 4.5); // d
		g.setColor(output[4]);
		drawLine(g, 1.5, 2.5, 1.5, 4.5); // e
		g.setColor(output[5]);
		drawLine(g, 1.5, 0.5, 1.5, 2.5); // f
		g.setColor(output[6]);
		drawLine(g, 1.5, 2.5, 3.5, 2.5); // g
		
		
		bound.setRect(coordx + 1, coordy, 2, 5);
		for(int i = 0; i < 4; i++) {
			input[i].paint(g);
		}
		 
	}

	@Override
	public void removeData() {
		for(int i = 0; i < 4; i++) {
			data.removeGateIO(input[i]);	
		}
		
	}

	@Override
	public void move(double x, double y) {
		coordx = x - 2;
		coordy = y - 2;
		
		bound.setRect(coordx + 1, coordy, 2, 5);
		for(int i = 0; i < 4; i++) {
			input[i].setPos(coordx, coordy + 1 + i);
		}
		
		
		//do something with node
	}
	
	public void sim1() {
		internalState = 0;
		if(input[0].getState() == Voltage.HIGH) internalState++;
		if(input[1].getState() == Voltage.HIGH) internalState += 2;
		if(input[2].getState() == Voltage.HIGH) internalState += 4;
		if(input[3].getState() == Voltage.HIGH) internalState += 8;
	}
	public void sim2() {
		if(internalState == 0) {
			output[0] = o;
			output[1] = o;
			output[2] = o;
			output[3] = o;
			output[4] = o;
			output[5] = o;
			output[6] = b;
		}
		else if(internalState == 1) {
			output[0] = b;
			output[1] = o;
			output[2] = o;
			output[3] = b;
			output[4] = b;
			output[5] = b;
			output[6] = b;
		}
		else if(internalState == 2) {
			output[0] = o;
			output[1] = o;
			output[2] = b;
			output[3] = o;
			output[4] = o;
			output[5] = b;
			output[6] = o;
		}
		else if(internalState == 3) {
			output[0] = o;
			output[1] = o;
			output[2] = o;
			output[3] = o;
			output[4] = b;
			output[5] = b;
			output[6] = o;
		}
		else if(internalState == 4) {
			output[0] = b;
			output[1] = o;
			output[2] = o;
			output[3] = b;
			output[4] = b;
			output[5] = o;
			output[6] = o;
		}
		else if(internalState == 5) {
			output[0] = o;
			output[1] = b;
			output[2] = o;
			output[3] = o;
			output[4] = b;
			output[5] = o;
			output[6] = o;
		}
		else if(internalState == 6) {
			output[0] = o;
			output[1] = b;
			output[2] = o;
			output[3] = o;
			output[4] = o;
			output[5] = o;
			output[6] = o;
		}
		else if(internalState == 7) {
			output[0] = o;
			output[1] = o;
			output[2] = o;
			output[3] = b;
			output[4] = b;
			output[5] = b;
			output[6] = b;
		}
		else if(internalState == 8) {
			output[0] = o;
			output[1] = o;
			output[2] = o;
			output[3] = o;
			output[4] = o;
			output[5] = o;
			output[6] = o;
		}
		else if(internalState == 9) {
			output[0] = o;
			output[1] = o;
			output[2] = o;
			output[3] = o;
			output[4] = b;
			output[5] = o;
			output[6] = o;
		}
		else if(internalState == 10) {
			output[0] = o;
			output[1] = o;
			output[2] = o;
			output[3] = b;
			output[4] = o;
			output[5] = o;
			output[6] = o;
		}
		else if(internalState == 11) {
			output[0] = b;
			output[1] = b;
			output[2] = o;
			output[3] = o;
			output[4] = o;
			output[5] = o;
			output[6] = o;
		}
		else if(internalState == 12) {
			output[0] = o;
			output[1] = b;
			output[2] = b;
			output[3] = o;
			output[4] = o;
			output[5] = o;
			output[6] = b;
		}
		else if(internalState == 13) {
			output[0] = b;
			output[1] = o;
			output[2] = o;
			output[3] = o;
			output[4] = o;
			output[5] = b;
			output[6] = o;
		}
		else if(internalState == 14) {
			output[0] = o;
			output[1] = b;
			output[2] = b;
			output[3] = o;
			output[4] = o;
			output[5] = o;
			output[6] = o;
		}
		else if(internalState == 15) {
			output[0] = o;
			output[1] = b;
			output[2] = b;
			output[3] = b;
			output[4] = o;
			output[5] = o;
			output[6] = o;
		}
	}
	
	public Element copy() {
		SevenSegment a = new SevenSegment(coordx, coordy);
		return a;
	}
}
