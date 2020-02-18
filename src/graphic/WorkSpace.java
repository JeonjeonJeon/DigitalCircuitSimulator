package graphic;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import element.AndGate;
import element.BufferGate;
import element.Element;
import element.Ground;
import element.Monitor;
import element.NandGate;
import element.NorGate;
import element.NotGate;
import element.OrGate;
import element.SevenSegment;
import element.SubCircuit;
import element.SubCircuitInput;
import element.SubCircuitOutput;
import element.Switch;
import element.TextBlock;
import element.TristateBufferGate;
import element.VPulse;
import element.Vdc;
import element.XorGate;
import main.GateIO;
import main.Node;
import main.Voltage;
import util.Calc;
import util.Rectangle;
import window.NavigationBar;
import window.Window;
import eventhandler.KeyboardHandle;

@SuppressWarnings("unchecked")

public class WorkSpace extends Canvas{
	private static final long serialVersionUID = 3147910908867708447L;
	private final String version = "190203";
	
	
	public static double ratio = 2; //increased or decreased when size of work space changes
	public static final double coordinateInterval = 10; //pixels between coordinate is coordinateInterval*ratio 

	public static double offsetX = 0;
	public static double offsetY = 0;
	//offset changes (ctrl down + mouse drag) which means camera movement
	
	public static ArrayList<Element> element = new ArrayList<Element>();
	public static ArrayList<GateIO> ios = new ArrayList<GateIO>();
	public static ArrayList<Node> node = new ArrayList<Node>();
	
	private double mPositionX = 0, mPositionY = 0;
	private double mx = 0, my = 0;//mouse coordinate
	private double dx = 0, dy = 0;
	
	private Node nodeTemp = null;
	private Rectangle dragBox = null;
	private Element ctrlCV = null;
	private Rectangle2D.Double coordDot = new Rectangle2D.Double();
	private NavigationBar nb = new NavigationBar();
	private String message;
	private boolean isSim = false;
	private boolean simConti = false;
	
	private boolean nodeMaking = false;
	private boolean movingOne = false;
	private boolean statusHide = true;
	
	//SubCircuit sc = new SubCircuit(7, 7);
	
	DropTarget dt; // drag and drop
	private KeyboardHandle kbHandle = new KeyboardHandle();
	private MouseHandle mHandle = new MouseHandle();
	
	public WorkSpace() {
		super();
		this.setBackground(Color.white);
		this.setFocusable(true);
		
		addMouseListener(mHandle);
		addKeyListener(kbHandle);
		addMouseMotionListener(mHandle);
		addMouseWheelListener(mHandle);
		dt = new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this, true, null);
		
		//sc.init(3, 3, null, null, null);
		//element.add(sc);
		
	}
	
	public void render(int fps) {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
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
			g.drawString("elements: " + element.size(), 0, lineNum);	lineNum += 12;
			g.drawString("nodes: " + node.size(), 0, lineNum);			lineNum += 12;
			g.drawString("gate io: " + ios.size(), 0, lineNum);			lineNum += 12;
			g.drawString("mouse: (" + mx + ", " + my + ") / (" + dx + ", " + dy + ")", 0, lineNum);	lineNum += 12;
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
		nb.mouseOver(mPositionX, mPositionY);
		
		g.dispose();
		bs.show();
	}
	
	public void paintComponent(Graphics2D g) throws ConcurrentModificationException {
		
		for(int i = 0; i < element.size(); i++) {
			element.get(i).paint(g);
		}
		
		for(int i = 0 ; i< node.size(); i++) {
			node.get(i).paint(g, mx, my);
		}
		
		if(nodeTemp != null) nodeTemp.paint(g, mx, my);
		
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
		for(Node nn : node) {
			nn.setState(Voltage.LOW);
		}
	}
	public void sim() {
		if(isSim == false) {
			System.out.println("press STARTSIM first");
			return;
		}
		for(Element ee : element) {
			ee.sim1();
		}
		for(Element ee : element) {
			ee.sim2();
		}
	}

	
	
	
	
	@Override
	
	public void drop(DropTargetDropEvent dtde) { // drag and drop
		if((dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0) {
			dtde.acceptDrop(dtde.getDropAction());
			Transferable tr = dtde.getTransferable();
			try {
				List<File> list = (List<File>) tr.getTransferData(DataFlavor.javaFileListFlavor);
				
				File f = list.get(0);
				FileInputStream in = new FileInputStream(f);
				ObjectInputStream oin = new ObjectInputStream(in);
				
				if(f.getAbsolutePath().endsWith(".dcs")) {
					
					Point2D.Double t = (Point2D.Double)(oin.readObject());
					offsetX = t.x;
					offsetY = t.y;
					ios = (ArrayList<GateIO>)(oin.readObject());
					element = (ArrayList<Element>)(oin.readObject());
					node = (ArrayList<Node>)(oin.readObject());
					int inputnum = 1;
					int outputnum = 1;
					for(Element el : element) {
						if(el instanceof SubCircuitInput) {
							inputnum++;
						}
						else if(el instanceof SubCircuitOutput) {
							outputnum++;
						}
					}
					SubCircuitInput.num = inputnum;
					SubCircuitOutput.num = outputnum;
					oin.close();
				}
				else if(f.getAbsolutePath().endsWith(".sc")) {
					getSCFile(f.getAbsolutePath(), getX(dtde.getLocation().x), getY(dtde.getLocation().y));
				}
				
			}catch(Exception eeeee) {
				eeeee.printStackTrace();
			}
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
		element.add(sc);
		oin.close();
	}

	
	
	private void nodeExtension() {
		if(nodeMaking == true) {
			for(Node n : node) {
				if(n.contains(mx, my)) {
					nodeMaking = false;
					nodeTemp.finishNode();
					n.mergeNode(nodeTemp);
					if(nodeTemp.type == 1) {
						node.remove(nodeTemp);
					}
					nodeTemp = null;
					return;
				}
			}
			for(GateIO io : ios) {//check if gato io is clicked
				if(io.contains(mx, my)) {//finishing node
					nodeMaking = false;
					nodeTemp.finishNode();
					io.setNode(nodeTemp);
					if(nodeTemp.type == 0) node.add(nodeTemp);
					nodeTemp = null;
					return;
				}
			}
			nodeTemp.nextLine();
		}
		
		else { //when nodeMaking is false
			for(Node n : node) { //call node
				if(n.contains(mx, my)) {
					nodeMaking = true;
					nodeTemp = n.callNode(mx, my);
					return;
				}
			}
			for(GateIO io : ios) { //make new node
				if(io.contains(mx, my)) {
					nodeMaking = true;
					nodeTemp = new Node(mx, my);
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
	
	
	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
	}
	@Override
	public void dragExit(DropTargetEvent dte) {
	}
	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		
	}
	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		
	}
}
