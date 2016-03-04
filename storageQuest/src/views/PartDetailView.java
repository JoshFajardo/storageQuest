package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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


public class PartDetailView extends MDIChild implements Observer {
	//Part object
	private Part myPart;
	
	
	private JLabel fldId;
	private JTextField fldPartName, fldPartNum,fldVendor,
	fldVendorPartNum, fldUnitQuanitity ;
	
	
	public PartDetailView(String title, Part p, MDIParent m) {
		super(title, m);
		
		myPart = p;

		//register as an observer
		myPart.addObserver(this);
		
		//how the fields are going to be laid out
		JPanel panel = new JPanel(); 
		panel.setLayout(new GridLayout(6, 2));
		
		//init fields to record data
		panel.add(new JLabel("Id"));
		fldId = new JLabel("");
		panel.add(fldId);
		
		panel.add(new JLabel("Part Name"));
		fldPartName = new JTextField("");
		fldPartName.addKeyListener(new TextfieldChangeListener());
		panel.add(fldPartName);
		
		panel.add(new JLabel("Part Number"));
		fldPartNum = new JTextField("");
		fldPartNum.addKeyListener(new TextfieldChangeListener());
		panel.add(fldPartNum);
		
		panel.add(new JLabel("Vendor"));
		fldVendor = new JTextField("");
		fldVendor.addKeyListener(new TextfieldChangeListener());
		panel.add(fldVendor);
		
		
		panel.add(new JLabel("Vendor Part Number"));
		fldVendorPartNum = new JTextField("");
		fldVendorPartNum.addKeyListener(new TextfieldChangeListener());
		panel.add(fldVendorPartNum);
		
		panel.add(new JLabel("Unit of Quanitity"));
		fldUnitQuanitity = new JTextField("");
		fldUnitQuanitity.addKeyListener(new TextfieldChangeListener());
		panel.add(fldUnitQuanitity);
		

		this.add(panel, BorderLayout.CENTER);
		
		//add a Save button 
		panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JButton button = new JButton("Save Record");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveModel();
			}
		});
		panel.add(button);
		
		this.add(panel, BorderLayout.SOUTH);

		//load fields with model info
		refreshFields();
		
	
		this.setPreferredSize(new Dimension(360, 210));
	}
	

	public void refreshFields() {
		fldId.setText("" + myPart.getId());
		fldPartName.setText(myPart.getPartName());
		fldPartNum.setText("" + myPart.getPartNumber());
		fldVendor.setText("" + myPart.getVendor());
		fldVendorPartNum.setText("" + myPart.getVendorPartNumber());
		fldUnitQuanitity.setText("" + myPart.getUnitQuanitity());
		
		
		

		// window title
		this.setTitle(myPart.getPartName());
		//flag as unchanged
		setChanged(false);
	}

	
	//this does roll back
	@Override
	public boolean saveModel() {
		//display any error message if field data are invalid
		String testName = fldPartName.getText().trim();
		
		if(!myPart.validPartName(testName)) {
			parent.displayChildMessage(Part.ERRORMSG_INVALID_PART_NAME);
			//when inserting new record, don't clear the fields
			if(myPart.getId() != Part.INVALID_ID)
				refreshFields();
			return false;
		}
		String testPartNum = fldPartNum.getText().trim();
		if(!myPart.validPartNumber(testPartNum)) {
			parent.displayChildMessage(Part.ERRORMSG_INVALID_PART_NUMBER);
			//when inserting new record, don't clear the fields
			if(myPart.getId() != Part.INVALID_ID)
				refreshFields();
			return false;
		}
		
		String testVendor = fldVendor.getText().trim();
		if(!myPart.validVendor(testVendor)) {
			parent.displayChildMessage(Part.ERRORMSG_INVALID_VENDOR);
			//when inserting new record, don't clear the fields
			if(myPart.getId() != Part.INVALID_ID)
				refreshFields();
			return false;
		}
		
		
		String testVendorPartNum = fldVendorPartNum.getText().trim();
		if(!myPart.validVendorPartNumber(testVendorPartNum)) {
			parent.displayChildMessage(Part.ERRORMSG_INVALID_VENDOR_PART_NUMBER);
			//when inserting new record, don't clear the fields
			if(myPart.getId() != Part.INVALID_ID)
				refreshFields();
			return false;
		}

		String testUQ = fldUnitQuanitity.getText().trim();
		if(!myPart.validUnitQuanitity(testUQ)) {
			parent.displayChildMessage(Part.ERRORMSG_INVALID_UNIT_QUANITITY);
			//when inserting new record, don't clear the fields
			if(myPart.getId() != Part.INVALID_ID)
				refreshFields();
			return false;
		}
		

		//fields are valid so save to model
		try {
			myPart.setPartName(testName);
			myPart.setPartNumber(testPartNum);
			myPart.setVendor(testVendor);
			myPart.setVendorPartNumber(testVendorPartNum);
			myPart.setUnitQuanitity(testUQ);
			
		} catch(Exception e) {
			parent.displayChildMessage(e.getMessage());
			refreshFields();
			return false;
		}
		
		//tell model that update is done (in case it needs to notify observers
		try {
			myPart.finishUpdate();
			setChanged(false);
			
		} catch (GatewayException e) {
			refreshFields();
			parent.displayChildMessage(e.getMessage());
			return false;
		}
		
		parent.displayChildMessage("Changes saved");
		return true;
	}


	@Override
	protected void cleanup() {
		//let superclass do its thing
		super.cleanup();
				
		//unregister from observable
		myPart.deleteObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		refreshFields();
	}

	public Part getMyPart() {
		return myPart;
	}

	public void setMyPart(Part myPart) {
		this.myPart = myPart;
	}
	
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
	}
}
