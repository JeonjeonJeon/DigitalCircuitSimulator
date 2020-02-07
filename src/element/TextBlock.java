package element;

import java.awt.Color;
import java.awt.Graphics2D;

public class TextBlock extends Element{
	private static final long serialVersionUID = 202389935507192736L;
	
	
	private String text = "Empty TextBlock";

	
	public TextBlock(double coorx, double coory) {
		super(coorx, coory);
	}

	@Override
	public void paint(Graphics2D g) {
		super.paint(g);
		
		g.setColor(Color.BLACK);
		
		drawLine(g, 0.2, 0.5, 3, 0.5);
		drawLine(g, 0.2, 1.5, 3, 1.5);
		drawLine(g, 0.2, 0.5, 0.2, 1.5);
		
		drawString(g, 0.3, 1.2, text);
		
		bound.setRect(coordx, coordy, 4, 2);
	}

	@Override
	public void removeData() {
		
	}

	@Override
	public void move(double x, double y) {
		coordx = x - 2;
		coordy = y - 2;
		
		bound.setRect(coordx, coordy, 4, 2);
		
		//do something with node
	}
	
	public void sim1() {
		
	}
	
	public void sim2() {
		
	}
	
	public String getText() {
		return text;
	}
	public void setText(String s) {
		text = s;
	}
	
	public Element copy() {
		TextBlock a = new TextBlock(coordx, coordy);
		return a;
	}
}
