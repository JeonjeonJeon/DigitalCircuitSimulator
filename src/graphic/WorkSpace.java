package graphic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import javax.swing.JPanel;

import datahandler.DataHandle;
import element.Element;
import element.NavigationBar;
import element.SubCircuit;
import element.SubCircuitInput;
import element.SubCircuitOutput;
import eventhandler.KeyboardHandle;
import eventhandler.MouseHandle;
import eventhandler.DragAndDropHandle;
import graphicComponent.Calc;
import graphicComponent.Rectangle;
import main.GateIO;
import main.Node;
import main.Voltage;

@SuppressWarnings("unchecked")

public class WorkSpace extends JPanel{
	private static final long serialVersionUID = 3147910908867708447L;
	private final String version = "190203";
	
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
	
	public boolean isSim = false;
	public boolean simConti = false;
	
	public boolean nodeMaking = false;
	public boolean movingOne = false;
	public boolean statusHide = true;
	
	//SubCircuit sc = new SubCircuit(7, 7);
	
	DropTarget dt; // drag and drop
	private KeyboardHandle kbHandle = new KeyboardHandle(this);
	private MouseHandle mHandle = new MouseHandle(this);
	private DragAndDropHandle dnd = new DragAndDropHandle(this);
	
	private DataHandle data = DataHandle.createInstance();
	
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
	
	public void render(int fps) {
		
		Graphics g = this.getGraphics();
		Graphics2D gg = (Graphics2D)g;
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT); 
		gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if(ratio > 3) gg.setStroke(new BasicStroke(2));
		else gg.setStroke(new BasicStroke(1)); 
		//init
		
		if(statusHide == false) {
			g.setColor(Color.BLACK);
			int lineNum = 12;
			g.drawString("Ver." + version, 0, lineNum); 	lineNum += 12;
			g.drawString("fps: " + fps, 0, lineNum); 	lineNum += 12;
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
			else g.drawString("nodeTemp: " + nodeTemp, 0, lineNum); 
			lineNum += 12;
			g.drawString("moving one: " + movingOne, 0, lineNum); lineNum += 12;
			g.drawString("node making: " + nodeMaking, 0, lineNum); lineNum += 12;
		}
		
		paintComponent(gg);
		nb.paint(gg);
		nb.mouseOver(mHandle.mPositionX, mHandle.mPositionY);
		
		g.dispose();
	}
	
	public void paintComponent(Graphics2D g) throws ConcurrentModificationException {
		
		for(int i = 0; i < data.elementSize(); i++) {
			data.getElement(i).paint(g);
		}
		
		for(int i = 0 ; i< data.nodeSize(); i++) {
			data.getNode(i).paint(g, mHandle.mx, mHandle.my);
		}
		
		if(nodeTemp != null) nodeTemp.paint(g, mHandle.mx, mHandle.my);
		
		g.setColor(Color.GREEN);
		if(dragBox != null) dragBox.draw(g);//draw drag box
		
		//draw coordinate point (integer interval)
		g.setColor(Color.BLACK);
		for(double dotx = 0 ; dotx < 50*ratio*coordinateInterval ; dotx += ratio*coordinateInterval) {
			for(double doty = 0 ; doty < 40*ratio*coordinateInterval ; doty += ratio*coordinateInterval) {
				coordDot.setRect(dotx - offsetX, doty - offsetY, 1, 1);
				g.fill(coordDot);
			}
		}
		
		if(simConti == true) {
			sim();
			/*
			for(Element ee : element) {
				if(ee instanceof SubCircuit) {
					SubCircuit sc = (SubCircuit) ee;
					sc.preSim();
				}
			}
			for(Element ee : element) {
				ee.sim1();
			}
			for(Element ee : element) {
				ee.sim2();
			}
			*/
		}
	}
	
	
	public void terminateSim() {
		if(isSim == false) {
			System.out.println("press STARTSIM first");
			return;
		}
		isSim = false;
		simConti = false;
		for(int i = 0; i < data.nodeSize(); i++) {
			data.getNode(i).setState(Voltage.LOW);
		}
	}
	public void sim() {
		if(isSim == false) {
			System.out.println("press STARTSIM first");
			return;
		}
		
		for(int i = 0; i < data.elementSize(); i++) {
			data.getElement(i).sim1();
		}
		for(int i = 0; i < data.elementSize(); i++) {
			data.getElement(i).sim2();
		}
	}

	public void getSCFile(String link, double mousex, double mousey) throws FileNotFoundException, IOException, ClassNotFoundException{
		SubCircuit sc = new SubCircuit(mousex, mousey);
		File f = new File(link);
		FileInputStream in = new FileInputStream(f);
		ObjectInputStream oin = new ObjectInputStream(in);
		oin.readObject(); // flush offset
		
		ArrayList<GateIO> gio = (ArrayList<GateIO>) oin.readObject();
		ArrayList<Element> el = (ArrayList<Element>) oin.readObject();
		ArrayList<Node> no = (ArrayList<Node>) oin.readObject();
		int tempIn = 0;
		int tempOut = 0;
		for(int i = 0 ; i < el.size() ; i++) {
			if(el.get(i) instanceof SubCircuitInput) tempIn++;
			else if(el.get(i) instanceof SubCircuitOutput) tempOut++;
		}
		sc.init(tempIn, tempOut, el, gio, no, f.getName().split("\\.")[0]);
		data.addElement(sc);
		oin.close();
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
