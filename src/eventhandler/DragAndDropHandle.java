package eventhandler;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import element.Element;
import element.SubCircuitInput;
import element.SubCircuitOutput;
import graphic.WorkSpace;
import main.GateIO;
import main.Node;

public class DragAndDropHandle  implements DropTargetListener {
	
	WorkSpace ws;
	
	public DragAndDropHandle(WorkSpace workspace) {
		ws = workspace;
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
