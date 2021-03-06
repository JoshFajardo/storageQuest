package controller;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.net.Authenticator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import database.GatewayException;
import login.User;
import login.ABACPolicy;
import login.SecurityException;
import login.Verify;
import login.Session;
import models.Warehouse;
import models.WarehouseList;
import models.Part;
import models.PartList;
import models.WarehousePart;
import reports.ReportException;
import reports.ReportGatewayMySQL;
import reports.WarehouseReportExcel;
import reports.WarehouseReportPDF;
import views.PartDetailView;
import views.PartListView;
import views.WarehousePartDetailView;
import views.WarehouseDetailView;
import views.WarehouseListView;


public class MDIParent extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private JDesktopPane desktop;
	private int newFrameX = 0, newFrameY = 0;
	
	private WarehouseList warehouseList;
	private WarehouseList newList;
	private PartList partList;
	
	private String name = "guest";
	private int num = 0;
	
	private List<MDIChild> openViews;
	
	public MDIParent (String title, WarehouseList wList, PartList pList){
		super(title);
		
		
		warehouseList = wList;
		newList = wList;
		partList = pList;
		
		
		openViews = new LinkedList<MDIChild>();

		
		MDIMenu menuBar = new MDIMenu(this);
		setJMenuBar(menuBar);
		
		desktop = new JDesktopPane();
		add(desktop);
		
		//this.addWindowListener(this);
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				closeProperly();
			}
		});
	}
	
	
	public void doCommand(MenuCommands cmd, Container caller){
		
		//Verify u = new Verify();
		
		
		ABACPolicy pol = new ABACPolicy();
		
		switch(cmd){
		case APP_QUIT:
			closeChildren();
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			break;
		case SHOW_LIST_WAREHOUSE:
			
			warehouseList.loadFromGateway();
			WarehouseListView v1 = new WarehouseListView("Warehouse List", new WarehouseListController(warehouseList),this,newList);
			
			openMDIChild(v1);
			break;
		case SHOW_DETAIL_WAREHOUSE :
			Warehouse w = ((WarehouseListView) caller).getSelectedWarehouse();
			w.fetchMyParts(warehouseList, partList);
			
			WarehouseDetailView v = new WarehouseDetailView(w.getFullName(),w,this);
			openMDIChild(v);
			break;
		
			
		case SHOW_LIST_PARTS :
			if(pol.canUserAccessFunction(name, "part.view")==false){
				displayChildMessage("Sorry "+name+" you don't have access");
				break;
			}
			partList.loadFromGateway();
			
			PartListView pv1 = new PartListView("Part List", new PartListController(partList),this);
			openMDIChild(pv1);
			break;
			
		case SHOW_DETAIL_PART:
			if(pol.canUserAccessFunction(name, "part.edit")==false){
				displayChildMessage("Sorry "+name+" you don't have access");
				break;
			}
			Part p = ((PartListView)caller).getSelectedPart();
			PartDetailView PartView = new PartDetailView(p.getPartName(),p,this);
			try{
				p.setLock(p);
			}catch(GatewayException e){
				System.err.println(e.getMessage());		
			}
			openMDIChild(PartView);
			break;
			
		case ADD_WAREHOUSE:
			Warehouse wAdd = new Warehouse("<name>", "<address>","<city>","<state>","<zip>", 0);
			
			warehouseList.addWarehouseToList(wAdd);
			warehouseList.addToNewRecords(wAdd);
			
			WarehouseDetailView vAdd = new WarehouseDetailView(wAdd.getFullName(), wAdd, this);
			openMDIChild(vAdd);
			break;
			
		case DELETE_WAREHOUSE:
			Warehouse wDelete = ((WarehouseListView)caller).getSelectedWarehouse();
			warehouseList.removeWarehouseFromList(wDelete);
			//close all the details that are based on this warehouse
			for(int i = openViews.size() - 1; i>=0; i--){
				MDIChild c = openViews.get(i);
				if(c instanceof WarehouseDetailView){
					//so if the detail view of the deleted object is showing, close it
					if(((WarehouseDetailView) c).getMyWarehouse().getId() == wDelete.getId())
						c.closeFrame();
				}
			}
			//delete warehouse from the database
			try{
				wDelete.delete();
				this.displayChildMessage("Warehouse Deleted");
			}catch (GatewayException e){
				System.err.println(e.getMessage());
				this.displayChildMessage("Error: Could not delete Warehouse!");
			}
			break;
		
		case ADD_PART:
			if(pol.canUserAccessFunction(name, "part.add")==false){
				displayChildMessage("Sorry "+name+" you don't have access");
				break;
			}
			Part pAdd = new Part();
			
			partList.addPartToList(pAdd);
			partList.addToNewRecords(pAdd);
			
			PartDetailView vPartAdd = new PartDetailView(pAdd.getPartName(), pAdd, this);
			openMDIChild(vPartAdd);
			break;
			
		case DELETE_PART:
			
			if(pol.canUserAccessFunction(name, "part.delete")==false){
				displayChildMessage("Sorry "+name+" you don't have access");
				break;
			}
			//first remove part, from the list
			Part pDelete = ((PartListView)caller).getSelectedPart();
			partList.removePartFromList(pDelete);
			
			//closes all details that are based on this object
			for(int i = openViews.size() - 1; i >= 0; i--){
				MDIChild c = openViews.get(i);
				if(c instanceof PartDetailView){
					if(((PartDetailView) c).getMyPart().getId() == pDelete.getId())
						c.closeFrame();
				}
			}
			
			try{
				pDelete.delete();
				this.displayChildMessage("Part Deleted!");
			}catch(GatewayException e){
				System.err.println(e.getMessage());
				this.displayChildMessage("Error, could not delete part!");
			}
			break;
			
		case SHOW_DETAIL_WAREHOUSE_PART:
			
			WarehousePart wp = ((WarehouseDetailView) caller).getSelectedWarehousePart();
	    	WarehousePartDetailView vWPart = new WarehousePartDetailView("", wp, this);
			openMDIChild(vWPart);
			break;
			
		case WAREHOUSE_REPORT_PDF:
			try{
				WarehouseReportPDF report = new WarehouseReportPDF(new ReportGatewayMySQL());
				report.generateReport();
				report.outputReportToFile("/Users/public/Desktop/report1.pdf");
				report.close();
			}catch (GatewayException | ReportException e) {
				this.displayChildMessage(e.getMessage());
				return;
			}
			break;
			
		case WAREHOUSE_REPORT_EXCEL:
			try{
				WarehouseReportExcel report = new WarehouseReportExcel(new ReportGatewayMySQL());
				report.generateReport();
				report.outputReportToFile("/Users/public/Desktop/report.xls");
				report.close();
			}catch(GatewayException | ReportException e){
				this.displayChildMessage(e.getMessage());
				return;
			}
			
			
		
		/*
		case LOGIN_AS_BOB:
			
			User user = new User("bob", "123456", "Boberino");
			try {			
				id = u.login(user.getLogin(), user.getPasswordHash());
			
				displayChildMessage("Hello bob ");
				
			} catch (SecurityException e) {
				//e.printStackTrace();
				displayChildMessage("Unable to login as bob");
			}
			break;
			*/
			
		/*
		case LOGIN_AS_SUE:
			//Verify u = new Verify();
			User user1 = new User("sue", "password", "pur-sue");
			
			try {
				id = u.login(user1.getLogin(), user1.getPasswordHash());
				boolean check = pol.canUserAccessFunction(user1.getLogin(), "part.view");
				displayChildMessage("Hello Sue" + check);
			} catch (SecurityException e) {
				//e.printStackTrace();
				displayChildMessage("Unable to login as sue");
			}
			break;
			
		case LOGIN_AS_RAGNAR:
			//Verify u = new Verify();
			User user2 = new User("ragnar", "viking4life", "ragBrok");
			
			try {
				id = u.login(user2.getLogin(), user2.getPasswordHash());
				boolean check = pol.canUserAccessFunction(user2.getLogin(), "part.add");
				displayChildMessage("Hello Ragnar" + check);
			} catch (SecurityException e) {
				//e.printStackTrace();
				displayChildMessage("Unable to login as ragnar");
			}
			break;
		case LOGOUT:
			
			u.logout(id);
			displayChildMessage("Logged out");
			break;
			*/
		}
		
	
		
	}
	public void closeChildren() {
		JInternalFrame [] children = desktop.getAllFrames();
		for(int i = children.length - 1; i >= 0; i--) {
			children[i].dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			//as each child window closes, it will call its closeChild() method to clean itself up
		}
	}

	/**
	 * Child windows are already closing so we just need to tell the child panels to clean up.
	 * This can happen when the JVM closes
	 */
	public void cleanupChildPanels() {
		JInternalFrame [] children = desktop.getAllFrames();
		for(int i = children.length - 1; i >= 0; i--) {
			if(children[i] instanceof MDIChildFrame){
				MDIChildFrame cf = (MDIChildFrame) children[i];
				cf.cleanup();
			}
				
		}
	}
	
	/**
	 * this method will always be called when the app quits since it hooks into the VM
	 */
	public void closeProperly() {
		cleanupChildPanels();
	}

	/**
	 * create the child panel, insert it into a JInternalFrame and show it
	 * @param child
	 * @return
	 */
	public JInternalFrame openMDIChild(MDIChild child) {
		//first, if child's class is single open only and already open,
		//then restore and show that child
		//System.out.println(openViewNames.contains(child));
		if(child.isSingleOpenOnly()) {
			for(MDIChild testChild : openViews) {
				if(testChild.getClass().getSimpleName().equals(child.getClass().getSimpleName())) {
					try {
						testChild.restoreWindowState();
					} catch(PropertyVetoException e) {
						e.printStackTrace();
					}
					JInternalFrame c = (JInternalFrame) testChild.getMDIChildFrame();
					return c;
				}
			}
		}
		
		//create then new frame
		MDIChildFrame frame = new MDIChildFrame(child.getTitle(), true, true, true, true, child);
		
		//pack works but the child panels need to use setPreferredSize to tell pack how much space they need
		//otherwise, MDI children default to a standard size that I find too small
		frame.pack();
		frame.setLocation(newFrameX, newFrameY);
		
		//tile its position
		newFrameX = (newFrameX + 10) % desktop.getWidth(); 
		newFrameY = (newFrameY + 10) % desktop.getHeight(); 
		desktop.add(frame);
		//show it
		frame.setVisible(true);
		
		//add child to openViews
		openViews.add(child);
		
		return frame;
	}
	
	
	public void displayChildMessage(String msg) {
		JOptionPane.showMessageDialog(this, msg);
	}
	
	public int confirmationMessage(Object msg,String title,int option){
		int response =JOptionPane.showConfirmDialog(this, msg, title, option);
		if(response == 1){
			return 1;
		}
		return 0;
		
	}
	
	public void removeFromOpenViews(MDIChild child) {
		openViews.remove(child);
	}
	

	public void getUserName(String Name){
		this.name = Name;
	}


}
