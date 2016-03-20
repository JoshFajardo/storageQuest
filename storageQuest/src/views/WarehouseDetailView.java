package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;

import controller.MDIChild;
import controller.MDIParent;
import controller.MenuCommands;
import database.GatewayException;
import models.Part;
import models.TransferablePart;
import models.Warehouse;
import models.WarehouseList;
import models.WarehousePart;

public class WarehouseDetailView extends MDIChild implements Observer{

	private Warehouse myWarehouse;
	
	
	private JLabel fldId;
	private JTextField fldWarehouseName, fldAddress, fldCity, fldState, fldZip;
	private JTextField fldStorageCap;
	
	private JList<WarehousePart> listMyParts;
	private WarehousePartListModel lmMyParts;
	
	private WarehousePart selectedModel;
	
	
	public WarehouseDetailView(String title, Warehouse warehouse, MDIParent m){
	super(title, m);
	
	myWarehouse = warehouse;
	
	myWarehouse.addObserver(this);
	
	JPanel panel = new JPanel();
	panel.setLayout(new GridLayout(7,2));
	
	
	panel.add(new JLabel("Id"));
	fldId = new JLabel("");
	panel.add(fldId);
	
	panel.add(new JLabel("Warehouse Name"));
	fldWarehouseName = new JTextField("");
	panel.add(fldWarehouseName);
	
	panel.add(new JLabel("Address"));
	fldAddress = new JTextField("");
	panel.add(fldAddress);
	
	panel.add(new JLabel("City"));
	fldCity = new JTextField("");
	panel.add(fldCity);
	
	panel.add(new JLabel("State"));
	fldState = new JTextField("");
	panel.add(fldState);
	
	panel.add(new JLabel("Zip"));
	fldZip = new JTextField("");
	panel.add(fldZip);
	
	panel.add(new JLabel("Storage Capacity"));
	fldStorageCap = new JTextField("");
	panel.add(fldStorageCap);
	
	this.add(panel,BorderLayout.CENTER);
	
	
	//adds parts in the warehouse list on the right side of the view
	lmMyParts = new WarehousePartListModel();
	listMyParts = new JList(lmMyParts);
	
	listMyParts.setDropMode(DropMode.INSERT);
	//used for drag and drop, not there yet
	listMyParts.setTransferHandler(new PartDropTransferHandler());
	
	//event handler for a double click
	listMyParts.addMouseListener(new MouseAdapter(){
		public void mouseClicked(MouseEvent evt){
			
			if(evt.getClickCount() == 2){
				int index = listMyParts.locationToIndex(evt.getPoint());
				//gets the warehouse at the index
				selectedModel = lmMyParts.getElementAt(index);
				
				openWarehousePartDetailView();
			}else{
				//if the use clicked the row's delete button
				Point pt = evt.getPoint();
				int index = listMyParts.locationToIndex(pt);
				//if the click was after the last row then don't try to process the delete
				Rectangle rect = listMyParts.getCellBounds(listMyParts.getModel().getSize() - 1, listMyParts.getModel().getSize() - 1);
				double listRowMaxY = rect.getY() + rect.getHeight();
				if(evt.getY() > listRowMaxY)
					return;
				
				WarehousePartListCellRenderer plcr = (WarehousePartListCellRenderer) listMyParts.getCellRenderer();
				if(plcr.mouseOnButton(evt)){
					selectedModel = lmMyParts.getElementAt(index);
					//delete prompt to user
					String [] options = {"Yes", "No"};
					if(JOptionPane.showOptionDialog(myFrame
	        				, "Do you really want to delete " + selectedModel.getPart().getPartName() + " ? This only deletes the part from the warehouse."
	        				, "Confirm Deletion"
	        				, JOptionPane.YES_NO_OPTION
	        			    , JOptionPane.QUESTION_MESSAGE
	        			    , null
	        			    , options
	        				, options[1]) == JOptionPane.NO_OPTION) {
	        			return;
	        		}
					
					selectedModel.getOwner().deletePart(selectedModel);
				}
			}
		}
	});
	
	listMyParts.setCellRenderer(new WarehousePartListCellRenderer());
	//size of the list in the window
	listMyParts.setPreferredSize(new Dimension(200, 200));
	panel = new JPanel();
	panel.add(new JScrollPane(listMyParts));
	this.add(panel, BorderLayout.EAST);
	
	
	panel = new JPanel();
	panel.setLayout(new FlowLayout());
	JButton saveButton = new JButton("Save!");
	saveButton.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e){
			saveModel();
		}
	});
	panel.add(saveButton);
	this.add(panel, BorderLayout.SOUTH);
	
	
	refreshFields();
	
	this.setPreferredSize(new Dimension(500, 230));
	
	}
	/*
	 * @TODO
	 */
	public void openWarehousePartDetailView(){
		parent.doCommand(MenuCommands.SHOW_DETAIL_WAREHOUSE_PART, this);
	}
	
	public WarehousePart getSelectedWarehousePart(){
		return selectedModel;
	}
	
	public void refreshFields() {
		fldId.setText("" + myWarehouse.getId());
		fldWarehouseName.setText(myWarehouse.getWarehouseName());
		fldAddress.setText(myWarehouse.getAddress());
		fldCity.setText(myWarehouse.getCity());
		fldState.setText(myWarehouse.getState());
		fldZip.setText(myWarehouse.getZip());
		fldStorageCap.setText("" + myWarehouse.getStorageCapacity());
		
		this.setTitle(myWarehouse.getWarehouseName());
		
		setChanged(false);
	}
	
	
	
	@Override
	public boolean saveModel(){
		String testWN = fldWarehouseName.getText().trim();
		if(!myWarehouse.validWarehouseName(testWN)){
			parent.displayChildMessage("Invalid Warehouse Name");
			refreshFields();
			return false;
		}
		String testAD = fldAddress.getText().trim();
		if(!myWarehouse.validAddress(testAD)){
			parent.displayChildMessage("Invalid Address");
			refreshFields();
			return false;
		}
		String testCT = fldCity.getText().trim();
		if(!myWarehouse.validCity(testCT)){
			parent.displayChildMessage("Invalid City");
			refreshFields();
			return false;
		}
		String testST = fldState.getText().trim();
		if(!myWarehouse.validState(testST)){
			parent.displayChildMessage("Invalid State");
			refreshFields();
			return false;
		}
		String testZip = fldZip.getText().trim();
		if(!myWarehouse.validZip(testZip)){
			parent.displayChildMessage("Invalid Zip");
			refreshFields();
			return false;
		}
		int testStorageCap = 0;
		try {
			testStorageCap = Integer.parseInt(fldStorageCap.getText());
		} catch(Exception e) {
			parent.displayChildMessage("Invalid Storage Capacity");
			refreshFields();
			return false;
		}
		if(!myWarehouse.validStorageCapacity(testStorageCap)) {
			parent.displayChildMessage("Invalid Storage capacity");
			refreshFields();
			return false;
		}
		
		//fields are valid at this point
		
		try{
			myWarehouse.setWarehouseName(testWN);
			myWarehouse.setAddress(testAD);
			myWarehouse.setCity(testCT);
			myWarehouse.setState(testST);
			myWarehouse.setZip(testZip);
			myWarehouse.setStorageCapacity(testStorageCap);
		}catch(Exception e){
			parent.displayChildMessage(e.getMessage());
			
			refreshFields();
			return false;
		}
		try{
		myWarehouse.finishUpdate();
		setChanged(false);
		//parent.displayChildMessage("Changes Saved");
		}catch(GatewayException e){
			refreshFields();
			parent.displayChildMessage(e.getMessage());
			return false;
		}
		parent.displayChildMessage("Changes saved");
		return true;
	}
	@Override
	protected void cleanup(){
		super.cleanup();
		
		myWarehouse.deleteObserver(this);
	}
	
	//this is called by the observable
	@Override
	public void update(Observable o, Object arg){
			refreshFields();
			
			lmMyParts.refreshContents();
	}
	
	public Warehouse getMyWarehouse(){
		return myWarehouse;
	}
	
	public void setMyWarehouse(Warehouse myWarehouse){
		this.myWarehouse = myWarehouse;
	}
	
	private class WarehousePartListModel extends AbstractListModel<WarehousePart>{
		
		@Override
		public int getSize(){
			return myWarehouse.getMyParts().size();
		}
		
		@Override
		public WarehousePart getElementAt(int index){
			return myWarehouse.getMyParts().get(index);
		}
		
		public void refreshContents(){
			this.fireContentsChanged(this, 0, this.getSize());
		}
		
	}
	
	private class PartDropTransferHandler extends TransferHandler{
		
		public boolean canImport (TransferSupport support){
			if(!support.isDrop()){
				return false;
			}
			return support.isDataFlavorSupported(TransferablePart.PART_FLAVOR);
		}
		
		public boolean importData(TransferSupport support){
			if(!canImport(support)) {
				return false;
			}
			
			//gets the drop location
			JList.DropLocation dl = (JList.DropLocation)support.getDropLocation();
			
			int index = dl.getIndex();
			
			//get the data needed
			
			Part data;
			try {
				data = (Part) support.getTransferable().getTransferData(TransferablePart.PART_FLAVOR);
				
			}catch (UnsupportedFlavorException e){
				e.printStackTrace();
				return false;
			}catch (java.io.IOException e){
				e.printStackTrace();
				return false;
			}
			
			boolean partAdded = myWarehouse.addPart(data);
			
			return true;
		}
	}
}
	
