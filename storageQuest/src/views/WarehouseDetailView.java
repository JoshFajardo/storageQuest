package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.MDIChild;
import controller.MDIParent;
import controller.MenuCommands;
import models.Warehouse;
import models.WarehouseList;

public class WarehouseDetailView extends MDIChild implements Observer{

	private Warehouse myWarehouse;
	
	private JLabel fldId;
	private JTextField fldWarehouseName, fldAddress, fldCity, fldState, fldZip;
	private JTextField fldStorageCap;
	
	
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
			saveWarehouse();
		}
	});
	panel.add(saveButton);
	this.add(panel, BorderLayout.SOUTH);
	
	
	refreshFields();
	
	this.setPreferredSize(new Dimension(360, 210));
	
	}
	
	public void refreshFields() {
		fldId.setText("" + myWarehouse.getId());
		fldWarehouseName.setText(myWarehouse.getWarehouseName());
		fldAddress.setText(myWarehouse.getAddress());
		fldCity.setText(myWarehouse.getCity());
		fldState.setText(myWarehouse.getState());
		fldZip.setText(myWarehouse.getZip());
		fldStorageCap.setText(myWarehouse.getStorageCapacity().toString());
		
		this.setTitle(myWarehouse.getWarehouseName());
	}
	
	
	
	
	public void saveWarehouse(){
		String testWN = fldWarehouseName.getText().trim();
		if(!myWarehouse.validWarehouseName(testWN)){
			parent.displayChildMessage("Invalid Warehouse Name");
			refreshFields();
			return;
		}
		String testAD = fldAddress.getText().trim();
		if(!myWarehouse.validAddress(testAD)){
			parent.displayChildMessage("Invalid Address");
			refreshFields();
			return;
		}
		String testCT = fldCity.getText().trim();
		if(!myWarehouse.validCity(testCT)){
			parent.displayChildMessage("Invalid City");
			refreshFields();
			return;
		}
		String testST = fldState.getText().trim();
		if(!myWarehouse.validState(testST)){
			parent.displayChildMessage("Invalid State");
			refreshFields();
			return;
		}
		String testZip = fldZip.getText().trim();
		if(!myWarehouse.validZip(testZip)){
			parent.displayChildMessage("Invalid Zip");
			refreshFields();
			return;
		}
		int testStorageCap = 0;
		try {
			testStorageCap = Integer.parseInt(fldStorageCap.getText());
		} catch(Exception e) {
			parent.displayChildMessage("Invalid Storage Capacity");
			refreshFields();
			return;
		}
		if(!myWarehouse.validStorageCapacity(testStorageCap)) {
			parent.displayChildMessage("Invalid Storage capacity");
			refreshFields();
			return;
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
			return;
		}
		
		myWarehouse.finishUpdate();
		parent.displayChildMessage("Changes Saved");
		}
	@Override
	protected void childClosing(){
		super.childClosing();
		
		myWarehouse.deleteObserver(this);
	}
	@Override
	public void update(Observable o, Object arg){
			refreshFields();
	}
	
}
	
