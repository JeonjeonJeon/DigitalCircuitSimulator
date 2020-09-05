package datahandler;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import element.And;
import element.Element;
import element.Nand;
import element.Nor;
import element.Or;
import element.SubCircuit;
import element.SubCircuitInput;
import element.SubCircuitOutput;
import element.Xor;
import framework.WorkSpace;
import main.Property;
import wires.GateIO;
import wires.Node;

// didn't check function
// just made class because of seperation

public class FileHandler {
	private DataHandler dh;
	private static FileHandler filehandler;

	private ArrayList<Element> element;
	private ArrayList<GateIO> ios;
	private ArrayList<Node> node;

	public static FileHandler getInstance() {
		if(filehandler == null) filehandler = new FileHandler();
		return filehandler;
		
	}

	private FileHandler() {
		dh = DataHandler.getInstance();
	}

	public boolean showDialog() {
		String message = "you haven't saved your changes.\nAre you sure you want load files?";
		int result = JOptionPane.showConfirmDialog(null, message, "Warning", JOptionPane.YES_NO_OPTION); 
		if(result == JOptionPane.CLOSED_OPTION) {
			System.out.println("dialog closed");
			return false;
		}
		else if(result == JOptionPane.YES_OPTION) {
			System.out.println("dialog yes");
			return true;
		}
		else if(result == JOptionPane.NO_OPTION) {
			System.out.println("dialog no");
			return false;
		}
		else return false;
	}
	public void getSCFile(String link, double mousex, double mousey) throws 
																	FileNotFoundException, 
																	IOException, 
																	ClassNotFoundException {
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
		dh.addElement(sc);
		oin.close();
	}

	public void fileSave() {
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
				//os.writeObject(new Point2D.Double(ws.offsetX, ws.offsetY));
				//os.writeObject(data.getIos());
				//os.writeObject(data.getElement());
				//os.writeObject(data.getNode());
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
	
	public void txtFileSave() {
		boolean exec = showDialog();
		if(exec == false) {
			return;
		}
		JFileChooser jfc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Digital Circuit Simulator", "dcs");
		FileNameExtensionFilter filter2 = new FileNameExtensionFilter("Sub Circuit", "sc");
		FileNameExtensionFilter filter3 = new FileNameExtensionFilter("Txt Sub Circuit", "txt");
		jfc.setFileFilter(filter);
		jfc.addChoosableFileFilter(filter3);
		jfc.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));
		int val = jfc.showSaveDialog(null);
		if(val == JFileChooser.APPROVE_OPTION) {
			try {
				File f = jfc.getSelectedFile();
				PrintWriter printer = new PrintWriter(f);
				
				printer.println("* Build Date : " + Property.currentDate);
				printer.println("* Author : " + System.getProperty("user.name") + "\n");
				for(Element element : dh.getElement()) {
					if(element instanceof And) {
						printer.println(element.toNetlist());
					}
					else if(element instanceof Nand) {
						printer.println(element.toNetlist());
					}
					else if(element instanceof Nor) {
						printer.println(element.toNetlist());
					}
					else if(element instanceof Or) {
						printer.println(element.toNetlist());
					}
					else if(element instanceof Xor) {
						printer.println(element.toNetlist());
					}
					printer.println("");
				}
				
				printer.println("");
				
				for(Node node : dh.getNode()) {
					printer.println(node.toSaveFile());
					printer.println("");
				}
				
				printer.close();
				
				
				if(jfc.getFileFilter().getDescription().startsWith("Digital" )&& f.getName().endsWith(".dcs") == false) {
					f.renameTo(new File(f.getAbsolutePath() + ".dcs"));
				}
				else if(jfc.getFileFilter().getDescription().startsWith("Sub") && f.getName().endsWith(".sc") == false){
					f.renameTo(new File(f.getAbsolutePath() + ".sc"));
				}
				else if(jfc.getFileFilter().getDescription().startsWith("Txt") && f.getName().endsWith(".txt") == false) {
					f.renameTo(new File(f.getAbsolutePath() + ".txt"));
				}
			}catch(Exception eee) {
				eee.printStackTrace();
			}
		}
	}
	public void txtFileOpen() {
		JFileChooser jfc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Digital Circuit Simulator", "dcs");
		FileNameExtensionFilter filter2 = new FileNameExtensionFilter("Sub Circuit", "sc");
		FileNameExtensionFilter filter3 = new FileNameExtensionFilter("Txt Sub Circuit", "txt");

		jfc.setFileFilter(filter);
		jfc.addChoosableFileFilter(filter3);
		jfc.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));
		int val = jfc.showOpenDialog(null);
		if(val == JFileChooser.APPROVE_OPTION) {

		try {
			File f = jfc.getSelectedFile();
			FileReader filereader = new FileReader(f);
			BufferedReader reader = new BufferedReader(filereader);
			
			FileInputStream in = new FileInputStream(f);
			ObjectInputStream oin = new ObjectInputStream(in);


			if(f.getAbsolutePath().endsWith(".txt")) {
				String line = "";
				
				ArrayList<GateIO> gateios = new ArrayList<GateIO>();
				ArrayList<Node> nodes = new ArrayList<Node>();
				ArrayList<Element> elements = new ArrayList<Element>();
				
				Node nodeTemp;
				Element elementTemp;

				int whatsReading = 0; // 0: nothing, 1: element, 2: node
				while((line = reader.readLine()) != null) {
					if(line.startsWith("*")) continue;
					else if(line.startsWith(".NODE")) {
						whatsReading = 2;
						nodeTemp = new Node();
						nodeTemp.name = line.split(" ")[1];
					}
					else if(whatsReading == 2) {
						if(line == "") {
							whatsReading = 0;
						}
						else {
							
							
						}
					}
				}
				
				reader.close();

				Point2D.Double t = (Point2D.Double)(oin.readObject());
				WorkSpace.offsetX = t.x;
				WorkSpace.offsetY = t.y;
				dh.setIos((ArrayList<GateIO>)(oin.readObject()));
				dh.setElement((ArrayList<Element>)(oin.readObject()));
				dh.setNode((ArrayList<Node>)(oin.readObject()));
				int inputnum = 1;
				int outputnum = 1;
				for(int i = 0; i < dh.elementSize(); i++) {
					Element el = dh.getElement(i);
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
			else {
				System.out.println("unable to open txt file");

			}
			oin.close();
			}catch(Exception eeee) {
				eeee.printStackTrace();
			}
		}
		
	}


}
