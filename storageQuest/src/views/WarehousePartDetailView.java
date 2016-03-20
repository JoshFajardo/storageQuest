package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.MDIChild;
import controller.MDIParent;
import database.GatewayException;

import models.Part;
import models.Warehouse;
import models.WarehousePart;

public class WarehousePartDetailView extends MDIChild implements Observer{
	
	private WarehousePart warehousePart;
	
	private JLabel fldWarehouseName;
	private JLabel fldPartNumber;
	private JTextField fldQuantity;
	
	

	public WarehousePartDetailView(String title, WarehousePart wp, MDIParent m){
		super(title,m);
		
		warehousePart = wp;
		//registers as an observer
		warehousePart.addObserver(this);
		//also registers as an observer to warehouse and part in case of change
		warehousePart.getOwner().addObserver(this);
		warehousePart.getPart().addObserver(this);
		
		//setting up the layout and fields
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3,2));
		
		panel.add(new JLabel("Warehouse Name"));
		fldWarehouseName = new JLabel(warehousePart.getOwner().getFullName());
		panel.add(fldWarehouseName);
		
		panel.add(new JLabel("Part Number"));
		fldPartNumber = new JLabel(warehousePart.getPart().getPartNumber());
		panel.add(fldPartNumber);
		
		panel.add(new JLabel("Quantity"));
		fldQuantity = new JTextField("");
		panel.add(fldQuantity);
		
		
		
		this.add(panel, BorderLayout.CENTER);
		
		panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JButton saveButton = new JButton("Save!");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				saveModel();
			}
		});
		panel.add(saveButton);
		
		this.add(panel, BorderLayout.SOUTH);
		
		refreshFields();
		
		this.setPreferredSize(new Dimension(450,150));
		
	}
	
	
	public void refreshFields(){
		fldWarehouseName.setText(warehousePart.getOwner().getFullName());
		fldPartNumber.setText(warehousePart.getPart().getPartNumber());
		fldQuantity.setText(""+warehousePart.getQuantity());
		
		this.setTitle(warehousePart.getOwner().getFullName() + " | " + warehousePart.getPart().getPartNumber());
		
		setChanged(false);
	}
	
	
	//saves changes to the view's part model
	
	@Override
	public boolean saveModel(){
		
		int testQ = 0;
		try{
			testQ =  Integer.parseInt(fldQuantity.getText());
		}catch (Exception e){
			parent.displayChildMessage("Could not change the Quantity");
			refreshFields();
			return false;
		}
		//fields are valid at this point, so save
		try {
			warehousePart.setQuantity(testQ);
		} catch(Exception e) {
			parent.displayChildMessage(e.getMessage());
			refreshFields();
			return false;
		}
		
		
		try{
			warehousePart.finishUpdate();
			setChanged(false);
			
		}catch (GatewayException e){
			
			refreshFields();
			parent.displayChildMessage(e.getMessage());
			return false;
		}
		parent.displayChildMessage("changes saved");
		return true;
	}
	
	
	
	@Override
	protected void cleanup() {
		
		super.cleanup();
				
		//unregister from observable
		warehousePart.deleteObserver(this);
		warehousePart.getOwner().deleteObserver(this);
		warehousePart.getPart().deleteObserver(this);
	}

	/**
	 * Called by Observable
	 */
	@Override
	public void update(Observable o, Object arg) {
		refreshFields();
	}

	public WarehousePart getWarehousePart() {
		return warehousePart;
	}

	public void setWarehousePart(WarehousePart wp) {
		this.warehousePart = wp;
	}
	/*
	private class TextfieldChangeListener implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
			//any typing in a text field flags view as having changed
			setChanged(true);
		}

		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}
	}*/
}
