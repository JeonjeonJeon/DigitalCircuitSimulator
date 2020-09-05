package datahandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import element.Element;
import element.SubCircuit;
import element.SubCircuitInput;
import element.SubCircuitOutput;
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

}
