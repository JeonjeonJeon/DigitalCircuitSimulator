package framework;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import datahandler.DataHandler;
import datahandler.FileHandler;
import element.Element;
import eventhandler.DragAndDropHandle;
import eventhandler.KeyboardHandle;
import eventhandler.MouseHandle;
import graphicComponent.Calc;
import graphicComponent.Rectangle;
import main.Main;
import wires.GateIO;
import wires.Node;

@SuppressWarnings("unchecked")

public class WorkSpace extends JPanel{
	private static final long serialVersionUID = 3147910908867708447L;
	private final String version = "200506";
	
	public static double ratio = 2; //increased or decreased when size of work space changes
	public static final double coordinateInterval = 10; //pixels between coordinate is coordinateInterval*ratio 

	public static double offsetX = 0;
	public static double offsetY = 0;
	//offset changes (ctrl down + mouse drag) which means camera movement
	
	
	public Node nodeTemp = null;
	public Rectangle dragBox = null;
	public Element ctrlCV = null;
	public Rectangle2D.Double coordDot = new Rectangle2D.Double();
	public NavigationBar nb = new NavigationBar();
	
	
	public boolean nodeMaking = false;
	public boolean movingOne = false;
	public boolean statusHide = false;
	
	//SubCircuit sc = new SubCircuit(7, 7);
	
	DropTarget dt; // drag and drop
	private KeyboardHandle kbHandle = new KeyboardHandle(this);
	private MouseHandle mHandle = new MouseHandle(this);
	private DragAndDropHandle dnd = new DragAndDropHandle(this);
	private FileHandler fh = FileHandler.getInstance();
	
	private DataHandler data = DataHandler.getInstance();
	
	int FPS;
	
	public WorkSpace() {
		super();
		this.setBackground(Color.white);
		this.setFocusable(true);
		
		addMouseListener(mHandle);
		addKeyListener(kbHandle);
		addMouseMotionListener(mHandle);
		addMouseWheelListener(mHandle);
		dt = new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, dnd, true, null);
		
	}
	
	
	@Override
	public void paintComponent(Graphics g){
		
		super.paintComponent(g);
		drawSystem(g);
		
		g.drawString("hello: " + System.currentTimeMillis()%10000, 100, 20);
		
		for(int i = 0; i < data.elementSize(); i++) {
			data.getElement(i).paint((Graphics2D)g);
		}
		
		for(int i = 0 ; i< data.nodeSize(); i++) {
			data.getNode(i).paint(g, mHandle.mx, mHandle.my);
		}
		
		if(nodeTemp != null) nodeTemp.paint(g, mHandle.mx, mHandle.my);
		
		g.setColor(Color.GREEN);
		if(dragBox != null) dragBox.draw(g);//draw drag box
		
		//draw coordinate point (integer interval)
		g.setColor(Color.BLACK);
//		for(double dotx = 0 ; dotx < 50*ratio*coordinateInterval ; dotx += ratio*coordinateInterval) {
//			for(double doty = 0 ; doty < 40*ratio*coordinateInterval ; doty += ratio*coordinateInterval) {
//				coordDot.setRect(dotx - offsetX, doty - offsetY, 1, 1);
//				((Graphics2D) g).fill(coordDot);
//			}
//		}
		
//		if(simConti == true) {
//			sim();
//		}
	}
	
	private void drawSystem(Graphics g) {
		Graphics2D gg = (Graphics2D) g;
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT); 
		gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if(ratio > 3) gg.setStroke(new BasicStroke(2));
		else gg.setStroke(new BasicStroke(1)); 
		//init
		
		if(statusHide == false) {
			g.setColor(Color.BLACK);
			int lineNum = 12;
			gg.drawString("Ver." + version, 0, lineNum); 	lineNum += 12;
			gg.drawString("fps: " + Main.FPS, 0, lineNum); 	lineNum += 12;
			g.drawString("elements: " + data.elementSize(), 0, lineNum);	lineNum += 12;
			g.drawString("nodes: " + data.nodeSize(), 0, lineNum);			lineNum += 12;
			g.drawString("gate io: " + data.iosSize(), 0, lineNum);			lineNum += 12;
			g.drawString("mouse: (" + mHandle.mx + ", " + mHandle.my + ") / (" + mHandle.dx + ", " + mHandle.dy + ")", 0, lineNum);	lineNum += 12;
			g.drawString("offset: (" + offsetX + ", " + offsetY + ")", 0, lineNum);	lineNum += 12;
			//g.drawString("element1Pos: (" + element.get(0).getX() + ", " + element.get(0).getY() + ")", 0, lineNum); lineNum += 12;	
			if(dragBox == null) g.drawString("dragbox: null", 0, lineNum);
			else g.drawString("dragbox: " + dragBox, 0, lineNum); 
			lineNum += 12;
			if(nodeTemp == null) g.drawString("nodeTemp: null", 0, lineNum);
			else g.drawString("nodeTemp: " + nodeTemp + " " + nodeTemp.name, 0, lineNum); 
			lineNum += 12;
			g.drawString("moving one: " + movingOne, 0, lineNum); lineNum += 12;
			g.drawString("node making: " + nodeMaking, 0, lineNum); lineNum += 12;
		}
		nb.paint(gg);
		
	}
	


	public void nodeExtension() {
		if(nodeMaking == true) {
			for(int i = 0; i < data.nodeSize(); i++) {
				Node n = data.getNode(i);
				if(n.contains(mHandle.mx, mHandle.my)) {
					nodeMaking = false;
					nodeTemp.finishNode();
					n.mergeNode(nodeTemp);
					if(nodeTemp.type == 1) {
						data.removeNode(nodeTemp);
					}
					nodeTemp = null;
					return;
				}
			}
			for(int i = 0; i < data.iosSize(); i++) { // check if gate io is clicked
				GateIO io = data.getIos(i);
				if(io.contains(mHandle.mx, mHandle.my)) {//finishing node
					nodeMaking = false;
					nodeTemp.finishNode();
					io.setNode(nodeTemp);
					if(nodeTemp.type == 0) data.addNode(nodeTemp);
					nodeTemp = null;
					return;
				}
			}
			nodeTemp.nextLine();
		}
		
		else { //when nodeMaking is false
			for(int i = 0; i < data.nodeSize(); i++) {
				Node n = data.getNode(i);
				if(n.contains(mHandle.mx, mHandle.my)) {
					nodeMaking = true;
					nodeTemp = n.callNode(mHandle.mx, mHandle.my);
					return;
				}
			}
			for(int i = 0; i < data.iosSize(); i++) {
				GateIO io = data.getIos(i);
				if(io.contains(mHandle.mx, mHandle.my)) {
					nodeMaking = true;
					nodeTemp = new Node(mHandle.mx, mHandle.my);
					io.setNode(nodeTemp);
					return;
				}
			}
		}
	}
	public double getX(double nx) {
		return Calc.stickX(nx);
	}
	public double getY(double ny) {
		return Calc.stickY(ny);
	}
}









/*
 * 
 * 
 * example of drawing object using JPanel (not canvas)

import javax.swing.*;
import java.awt.*;

public class MyClass implements Runnable
{
   private JFrame mainFrame;
   private JPanel drawingPanel;   
   private boolean running;

   private Rectangle rect;
   private int xDirection;
   private int yDirection;
   private int xSpeed;
   private int ySpeed;
   
   public MyClass()
   {
      rect = new Rectangle(50, 50, 50, 50);

      mainFrame = new JFrame("Test");
      drawingPanel = new DrawingPanel();
      drawingPanel.setPreferredSize(new Dimension(640, 480));
      mainFrame.setContentPane(drawingPanel);
      mainFrame.pack();
      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      mainFrame.setVisible(true);
      
      xDirection = 1;
      yDirection = 1;
      xSpeed = 2;
      ySpeed = 2;
      Thread mainLoop = new Thread(this);
      mainLoop.start();
   }

   public void run()
   {
      running = true;
      while(running)
      {
         updatePositions();
         drawingPanel.repaint();
         try
         {
            Thread.sleep(15);
         } catch(InterruptedException ex) {
            ex.printStackTrace();
         }
      }
   }

   private void updatePositions()
   {
      rect.x += xDirection*xSpeed;
      rect.y += yDirection*ySpeed;
      
      if(rect.x < 0)
      {
         rect.x = 0;
         xDirection = 1;
      }
      else if(rect.x+rect.width > drawingPanel.getWidth())
      {
         rect.x = drawingPanel.getWidth()-rect.width;
         xDirection = -1;
      }

      if(rect.y < 0)
      {
         rect.y = 0;
         yDirection = 1;
      }
      else if(rect.y+rect.height > drawingPanel.getHeight())
      {
         rect.y = drawingPanel.getHeight()-rect.height;
         yDirection = -1;
      }
   }
   
   public static void main(String[] args)
   {
      new MyClass();
   }

   class DrawingPanel extends JPanel
   {
      public void paintComponent(Graphics g)
      {
         super.paintComponent(g);
         Graphics2D g2 = (Graphics2D)g;
         
         //draw black background
         g2.setColor(Color.BLACK);
         g2.fillRect(0, 0, drawingPanel.getWidth(),  drawingPanel.getHeight());
         
         //draw red rectangle
         g2.setColor(Color.RED);
         g2.fillRect(rect.x, rect.y, rect.width, rect.height);
      }
   }
}



*/
