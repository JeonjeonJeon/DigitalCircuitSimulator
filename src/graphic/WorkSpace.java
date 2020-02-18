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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import main.Main;
import main.GateIO;
import main.Node;
import main.Voltage;
import util.Calc;
import util.Rectangle;

@SuppressWarnings("unchecked")

public class WorkSpace extends Canvas implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener, Serializable, DropTargetListener {
	private static final long serialVersionUID = 3147910908867708447L;
	private final String version = "190203";
	DropTarget dt; // drag and drop
	
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
	
	public WorkSpace() {
		super();
		this.setBackground(Color.white);
		this.setFocusable(true);
		
		addMouseListener(this);
		addKeyListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
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
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		for(Element ee : element) {
			if(ee.contains(mx, my)) {
				if(ee instanceof SubCircuitInput) {
					SubCircuitInput sci = (SubCircuitInput) ee;
					if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) sci.setID(sci.getID().substring(0, sci.getID().length()-1));
					else if(e.getKeyCode() == KeyEvent.VK_SPACE) sci.setID(sci.getID() + " ");
					else sci.setID(sci.getID() + KeyEvent.getKeyText(e.getKeyCode()));
					return;
				}
				else if(ee instanceof SubCircuitOutput) {
					SubCircuitOutput sco = (SubCircuitOutput) ee;
					if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) sco.setID(sco.getID().substring(0, sco.getID().length()-1));
					else if(e.getKeyCode() == KeyEvent.VK_SPACE) sco.setID(sco.getID() + " ");
					else sco.setID(sco.getID() + KeyEvent.getKeyText(e.getKeyCode()));
					return;
				}
				else if(ee instanceof TextBlock) {
					TextBlock tb = (TextBlock) ee;
					if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) tb.setText(tb.getText().substring(0, tb.getText().length()-1));
					else if(e.getKeyCode() == KeyEvent.VK_SPACE) tb.setText(tb.getText() + " ");
					else tb.setText(tb.getText() + KeyEvent.getKeyText(e.getKeyCode()));
					return;
				}
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			ratio *= 1.1;
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			ratio *= 0.9;
		}
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			for(Element ee : element) {
				if(ee.contains(mx, my)) {
					if(ee instanceof VPulse) {
						VPulse vp = (VPulse) ee;
						vp.setPulseWidth(vp.getPulseWidth() + 0.5);
					}
				}
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			for(Element ee : element) {
				if(ee.contains(mx, my)) {
					if(ee instanceof VPulse) {
						VPulse vp = (VPulse) ee;
						vp.setPulseWidth(vp.getPulseWidth() - 0.5);
					}
				}
			}
		}
		else if(e.isControlDown() == true) {
			if(e.getKeyCode() == KeyEvent.VK_C) {
				for(Element ee : element) {
					if(ee.isSelected() == true) {
						ctrlCV = ee;
						return;
					}
				}
			}
			else if(e.getKeyCode() == KeyEvent.VK_V) {
				if(ctrlCV != null) {
					Element copied = ctrlCV.copy();
					copied.move(ctrlCV.getX() + 2, ctrlCV.getY() + 4);
					ctrlCV = copied;
					element.add(copied);
				}
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_H) {
			statusHide = !statusHide;
		}
		else if(e.getKeyCode() == KeyEvent.VK_L) {
			DigitalCircuitSimulator.FPS_LOCK = !DigitalCircuitSimulator.FPS_LOCK;
		}
		else if(e.getKeyCode() == KeyEvent.VK_W) {
			nodeExtension();
		}
		else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			sim();
		}
		else if(e.getKeyCode() == KeyEvent.VK_P) {
			for(Element ee : element) {
				if(ee.contains(mx, my)) {
					System.out.println(ee.toString());
				}
			}
			for(Node nn : node) {
				if(nn.contains(mx, my)) {
					System.out.println(nn.toString());
				}
			}
			for(GateIO io : ios) {
				if(io.contains(mx, my)) {
					System.out.println(io.toString());
				}
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_C) {
			for(Element ee : element) {
				if(ee instanceof Switch) {
					Switch ss = (Switch)ee;
					if(ss.contains(mx, my)) ss.changeStatus();
				}
				else if(ee instanceof Vdc) {
					Vdc vdc = (Vdc)ee;
					if(vdc.contains(mx, my)) vdc.changeStatus();
				}
				else if(ee instanceof VPulse) {
					VPulse vp = (VPulse) ee;
					if(vp.contains(mx, my)) vp.setPulseWidth(1);
				}
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_DELETE) {
			for(int i = 0 ; i < element.size() ; i++) {
				if(element.get(i).isSelected()) {
					element.get(i).removeData();
					element.remove(i);
					i--;
				}
			}
			for(int i = 0 ; i < node.size(); i++) {
				if(node.get(i).isSelected()) {
					for(GateIO io : ios) {
						if(io.isLinked() && io.getNode().equals(node.get(i))) {
							io.setNode(null);
						}
					} 
					node.remove(i);
					i--;
				}
			}
		}
		
		nb.keyPressed(e.getKeyCode());
	}

	
	@Override
	public void mousePressed(MouseEvent e) {
		dx = mx;
		dy = my;
		if(message == "OR") {
			if(isSim == true) terminateSim();
			element.add(new OrGate(mx, my));
		}
		else if(message == "AND") {
			if(isSim == true) terminateSim();
			element.add(new AndGate(mx, my));
		}
		else if(message == "VPULSE") {
			if(isSim == true) terminateSim();
			element.add(new VPulse(mx, my));
		}
		else if(message == "GND") {
			if(isSim == true) terminateSim();
			element.add(new Ground(mx, my));
		}
		else if(message == "SWITCH") {
			if(isSim == true) terminateSim();
			element.add(new Switch(mx, my));
		}
		else if(message == "VDC") {
			if(isSim == true) terminateSim();
			element.add(new Vdc(mx, my));
		}
		else if(message == "MONITOR") {
			if(isSim == true) terminateSim();
			element.add(new Monitor(mx, my));
		}
		else if(message == "NOT") {
			if(isSim == true) terminateSim();
			element.add(new NotGate(mx, my));
		}
		else if(message == "INPUT") {
			if(isSim == true) terminateSim();
			element.add(new SubCircuitInput(mx, my));
		}
		else if(message == "OUTPUT") {
			if(isSim == true) terminateSim();
			element.add(new SubCircuitOutput(mx, my));
		}
		else if(message == "NAND") {
			if(isSim == true) terminateSim();
			element.add(new NandGate(mx, my));
		}
		else if(message == "NOR") {
			if(isSim == true) terminateSim();
			element.add(new NorGate(mx, my));
		}
		else if(message == "BUFFER") {
			if(isSim == true) terminateSim();
			element.add(new BufferGate(mx, my));
		}
		else if(message == "XOR") {
			if(isSim == true) terminateSim();
			element.add(new XorGate(mx, my));
		}
		else if(message == "TRISTATE") {
			if(isSim == true) terminateSim();
			element.add(new TristateBufferGate(mx, my));
		}
		else if(message == "TEXT") {
			if(isSim == true) terminateSim();
			element.add(new TextBlock(mx, my));
		}
		else if(message == "SEVEN") {
			if(isSim == true) terminateSim();
			element.add(new SevenSegment(mx, my));
		}
		else if(message != null && message.endsWith("NULL")) {
			
		}
		else {
			try {
				getSCFile(message, mx, my);
			}
			catch(Exception exc) {
			}
			
		}
		
		
		message = nb.mouseClicked(e.getX(), e.getY());
		
		
		if(message == "SIM_START") {
			isSim = true;
			for(Node nn : node) {
				if(nn.startSim() == false) {
					isSim = false;
					System.out.println("Error found at some nodes. Simulation failed!!");
					return;
				}
			}
			for(Node nn : node) {
				nn.setState(Voltage.LOW);
			}
			for(Element ee : element) {
				if(ee instanceof Monitor) {
					Monitor m = (Monitor)ee;
					m.getLinkedNode().setState(Voltage.LOW);
				}
				else if(ee instanceof Vdc) {
					Vdc vdc = (Vdc)ee;
					vdc.sim1();
					vdc.sim2();
				}
				else if(ee instanceof SubCircuit) {
					SubCircuit sc = (SubCircuit) ee;
					sc.simInit(Voltage.LOW);
				}
			}
		}
		
		else if(message == "SIM_STEP_INTO") {
			sim();
		}
		else if(message == "SIM_10_STEP") {
			if(isSim == false) {
				System.out.println("press STARTSIM first");
				return;
			}
			for(int i = 0; i < 10; i++) {
				sim();
			}
			
		}
		else if(message == "SIM_CONTINUOUS") {
			if(isSim == false) {
				System.out.println("press SIM_START first");
				return;
			}
			simConti = true;
		}
		else if(message == "SIM_END"){
			terminateSim();
		}
		else if(message == "FILE_SAVE") {
			terminateSim();
			JFileChooser jfc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Digital Circuit Simulator", "dcs");
			FileNameExtensionFilter filter2 = new FileNameExtensionFilter("Sub Circuit", "sc");
			jfc.setFileFilter(filter);
			jfc.addChoosableFileFilter(filter2);
			jfc.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));
			int val = jfc.showSaveDialog(null);
			if(val == JFileChooser.APPROVE_OPTION) {
				try {
					File f = jfc.getSelectedFile();
					FileOutputStream filestream = new FileOutputStream(f);
					ObjectOutputStream os = new ObjectOutputStream(filestream);
					os.writeObject(new Point2D.Double(offsetX, offsetY));
					os.writeObject(ios);
					os.writeObject(element);
					os.writeObject(node);
					os.close();
					filestream.close();
					
					if(jfc.getFileFilter().getDescription().startsWith("Digital" )&& f.getName().endsWith(".dcs") == false) {
						f.renameTo(new File(f.getAbsolutePath() + ".dcs"));
					}
					else if(jfc.getFileFilter().getDescription().startsWith("Sub") && f.getName().endsWith(".sc") == false){
						f.renameTo(new File(f.getAbsolutePath() + ".sc"));
					}
				}catch(Exception eee) {
					eee.printStackTrace();
				}
			}
		}
		else if(message == "FILE_OPEN") {
			JFileChooser jfc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Digital Circuit Simulator", "dcs");
			FileNameExtensionFilter filter2 = new FileNameExtensionFilter("Sub Circuit", "sc");
			jfc.setFileFilter(filter);
			jfc.addChoosableFileFilter(filter2);
			jfc.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));
			int val = jfc.showOpenDialog(null);
			if(val == JFileChooser.APPROVE_OPTION) {

			try {
				File f = jfc.getSelectedFile();
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
				}
				else if(f.getAbsolutePath().endsWith(".sc")) {
					message = f.getAbsolutePath();
				}
				oin.close();
				}catch(Exception eeee) {
					eeee.printStackTrace();
				}
			}
		}
		dragBox = new Rectangle(getX(mx), getY(my), getX(mx)-getX(dx), getY(my)-getY(dy));
		for(Element el : element) {
			if(el.contains(mx, my)) {
				movingOne = true;
			}
			else if(el.isSelected()) {
				
			}
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
	public void mouseReleased(MouseEvent e) {
		movingOne = false;
		if(dragBox != null) {
			if(dragBox.isEmpty()) {
				dragBox = null;
				return;
			}
			for(Element el : element) {
				el.select(dragBox);//select elements which intersects with dragbox
			}
			for(Node n : node) {
				n.select(dragBox);
			}
			dragBox = null;//erase dragbox
		}
		
		mx = e.getX();
		my = e.getY();
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		//bug: call node and merge itself - ConcurrentModificationException
		//bug: double clicking one gate io - NegativeArraySizeException
		nodeExtension();
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		mPositionX = e.getX();
		mPositionY = e.getY();
		mx = getX(mPositionX);
		my = getY(mPositionY);
		
		if(nodeMaking == false) {
			for(Element el : element) {
				if(el.contains(mx, my) == true) break;
			}
			for(GateIO gio : ios) {
				gio.contains(mx, my);
			}
			for(Node n : node) {
				n.contains(mx, my);
			}
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) { // little bit awkward when control key is down
		double diffx = mPositionX - e.getX();
		double diffy = mPositionY - e.getY();
		mPositionX = e.getX();
		mPositionY = e.getY();
		dx = getX(mPositionX);
		dy = getY(mPositionY);
		
		if(e.isControlDown()) {
			offsetX += diffx;
			offsetY += diffy;
		}
		else if(movingOne) {
			for(Element el : element) {
				if(el.containing) {
					el.move(dx, dy);
					break;
				}
			}
		}
		else {
			if(dragBox != null) {
				dragBox.setRect(Calc.min(mx, dx), 
						Calc.min(my, dy), 
						Calc.abs(mx - dx), 
						Calc.abs(my - dy));
			}
		}
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int n = e.getWheelRotation();
		if(n < 0) {
			ratio *= 1.1;
		}
		else {
			ratio *= 0.9;
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

	@Override
	public void keyReleased(KeyEvent e) {	
	}
	@Override
	public void keyTyped(KeyEvent e) {
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
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
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
