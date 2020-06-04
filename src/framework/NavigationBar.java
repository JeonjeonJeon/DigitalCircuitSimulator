package framework;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import graphicComponent.Calc;
import main.Property;

public class NavigationBar {
	
	double outlineWidth = 100;
	double outlineHeight = 100;
	double x = Property.WIDTH - outlineWidth - 20;
	int xmin = Property.WIDTH - 140;
	int xmax = Property.WIDTH -30;
	int boundMax = Property.WIDTH - 90;
	
	RoundRectangle2D.Double outline;
	RoundRectangle2D.Double[][] icons;
	String[][] lable = new String[17][2];
	int ci = -1, cj = -1;
	Rectangle2D.Double bound;
	boolean directionRight = true;
	boolean autoHide = false;
	double counter = 0;
	
	public NavigationBar() {
		outline = new RoundRectangle2D.Double(x, 10, outlineWidth, Property.HEIGHT - 100, 30, 30);
		bound = new Rectangle2D.Double(boundMax, 0, Property.WIDTH-xmin, Property.HEIGHT);
		
		icons = new RoundRectangle2D.Double[17][2];
		for(int i = 0 ; i < 17 ; i++) {
			for(int j = 0 ; j < 2 ; j++) {
				icons[i][j] = new RoundRectangle2D.Double(outline.x + 10+j*45, outline.y + 22+i*45, 35, 35, 10, 10);
				lable[i][j] = "NULL";
			}
		}
		int lineNum = 0;
		lable[lineNum][0] = "OR";			lable[lineNum][1] = "AND";				lineNum++;
		lable[lineNum][0] = "VPULSE";		lable[lineNum][1] = "GND";				lineNum++;
		lable[lineNum][0] = "SWITCH";		lable[lineNum][1] = "VDC";				lineNum++;
		lable[lineNum][0] = "MONITOR";		lable[lineNum][1] = "NOT";				lineNum++;
		lable[lineNum][0] = "NAND";			lable[lineNum][1] = "NOR";				lineNum++;
		lable[lineNum][0] = "BUFFER";		lable[lineNum][1] = "XOR";				lineNum++;
		lable[lineNum][0] = "INPUT";		lable[lineNum][1] = "OUTPUT";			lineNum++;
		lable[lineNum][0] = "TRISTATE";		lable[lineNum][1] = "TEXT";				lineNum++;
		lable[lineNum][0] = "SEVEN";												lineNum++;
																					lineNum++;
		lable[lineNum][0] = "SIM_START";		lable[lineNum][1] = "SIM_END";		lineNum++;
		lable[lineNum][0] = "SIM_STEP_INTO";	lable[lineNum][1] = "SIM_10_STEP";	lineNum++;
		lable[lineNum][0] = "SIM_CONTINUOUS";										lineNum++;
																					lineNum++;
		lable[lineNum][0] = "FILE_SAVE";	lable[lineNum][1] = "FILE_OPEN";
	}

	public void paint(Graphics2D g) {
		
		g.setColor(Color.GRAY);
		g.fill(outline);
		g.setColor(Color.WHITE);
		for(int i = 0 ; i < 17 ; i++) {
			for(int j = 0 ; j < 2 ; j++) {
				if(ci == i && cj == j && lable[i][j].startsWith("SIM") == false && lable[i][j].startsWith("FILE") == false) { // when step into button is clicked don't change icon color
					g.setColor(Color.GREEN);
				}
				else g.setColor(Color.WHITE);
				g.fill(icons[i][j]);
				g.setColor(Color.BLACK);
				if(lable[i][j] != "NULL") {
					String[] s = lable[i][j].split("_");
					if(s.length >= 2) {
						for(int ii = 0 ; ii < s.length ; ii++) {
							g.drawString(s[ii], (int)(icons[i][j].x + 3), (int)(icons[i][j].y + 10 + 11*ii));
						}
					}
					else g.drawString(lable[i][j], (int)(icons[i][j].x + 3), (int)(icons[i][j].y + 10));
				}
			}
		}
		//g.setColor(Color.RED);
		//g.draw(bound);
	}
	
	public double sinMap(double n) {
		if(directionRight == true) {
			return xmin + (xmax-xmin)*Math.sin((Math.PI*n)/(2*(xmax-xmin)));
		}
		else {
			return xmin + (xmax-xmin)*Math.sin((Math.PI*n)/(2*(xmax-xmin) - Math.PI/2));
		}
	}
	
	public void mouseOver(double mx, double my) {
		if(autoHide == true) {
			if(bound.contains(mx, my) == true) { // counter 0 -> xmin / counter max-min -> xmax
				directionRight = false;
				counter = counter - 5;
				counter = Calc.max(0, counter);
				outline.x = Calc.max(xmin, sinMap(counter));
				bound.x = Calc.min(outline.x, boundMax);
			}
			else {
				directionRight = true;
				counter = counter + 5;
				counter = Calc.min(xmax - xmin, counter);
				outline.x = Calc.min(xmax, sinMap(counter));
			}
		}
		else {
			outline.x = xmin;
		}
		for(int i = 0 ; i < 17 ; i++) {
			for(int j = 0 ; j < 2 ; j++) {
				icons[i][j].setRoundRect(outline.x+10+j*45, outline.y+22+i*45, 35, 35, 10, 10);
			}
		}
	}
	
	public void keyPressed(int key) {
		if(key == KeyEvent.VK_N) {
			autoHide = !autoHide;
		}
	}
	
	public String mouseClicked(double mx, double my) {
		for(int i = 0 ; i < 17 ; i++) {
			for(int j = 0 ; j < 2 ; j++) {
				if(icons[i][j].contains(mx, my) == true) {
					ci = i;
					cj = j;
					
					return lable[ci][cj];
				}
			}
		}
		ci = -1;
		cj = -1;
		return "NULL";
	}
}
