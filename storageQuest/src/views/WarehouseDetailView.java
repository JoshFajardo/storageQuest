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
	
	this.add(panel);
	
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
	
	this.setPreferredSize(new Dimension(360, 210));
	
	}
	/*
	 * @TODO
	 */
	public void openWarehousePartDetailView(){
		//parent.doCommand(MenuCommands.SHOW_DETAIL_WAREHOUSE_PART, this);
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
			System.out.println("line 200");
			refreshFields();
			return false;
		}
		try{
		myWarehouse.finishUpdate();
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
	@Override
	public void update(Observable o, Object arg){
			refreshFields();
			
			//lmMyParts.refreshContents();
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
	
	
	
}
	
