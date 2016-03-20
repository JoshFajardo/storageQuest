package models;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class TransferablePart implements Transferable {
	private Part part;
	//data flavor provides meta info about the data
	public static final DataFlavor PART_FLAVOR = new DataFlavor(Part.class, "A Part object");
	
	protected static DataFlavor[] supportedFlavors = {PART_FLAVOR, DataFlavor.stringFlavor};
	
	public TransferablePart(Part p){
		part = p;
	}
	
	@Override
	public DataFlavor[] getTransferDataFlavors(){
		return supportedFlavors;
	}
	
	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor){
		if(flavor.equals(PART_FLAVOR) || flavor.equals(DataFlavor.stringFlavor))
				return true;
		return false;
	}
	
	
	@Override
	public Object getTransferData (DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		if(flavor.equals(PART_FLAVOR))
			return part;
		else if(flavor.equals(DataFlavor.stringFlavor))
			return part.toString();
		else
			throw new UnsupportedFlavorException(flavor);
	}

	
}
