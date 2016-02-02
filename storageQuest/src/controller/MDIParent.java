package controller;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
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

import models.Warehouse;
import models.WarehouseList;
import views.WarehouseDetailView;
import views.WarehouseListView;


public class MDIParent extends JFrame{
	private static final long serialVersionUID = 1L;
	private JDesktopPane desktop;
	private int newFrameX = 0, newFrameY = 0;
	
	private WarehouseList warehouseList;
	
	private List<MDIChild> openViews;
	
	public MDIParent (String title, WarehouseList wList){
		super(title);
		
		
		warehouseList = wList;
		
		openViews = new LinkedList<MDIChild>();
		
		MDIMenu menuBar = new MDIMenu(this);
		setJMenuBar(menuBar);
		
		desktop = new JDesktopPane();
		add(desktop);
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				closeProperly();
			}
		});
	}
	
	
	public void doCommand(MenuCommands cmd, Container caller){
		
		switch(cmd){
		case APP_QUIT:
			closeChildren();
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			break;
		case SHOW_LIST_WAREHOUSE:
			WarehouseListView v1 = new WarehouseListView("Warehouse List", new WarehouseListController(warehouseList),this);
			
			openMDIChild(v1);
			break;
		case SHOW_DETAIL_WAREHOUSE :
			Warehouse w = ((WarehouseListView) caller).getSelectedWarehouse();
			WarehouseDetailView v = new WarehouseDetailView(w.getFullName(),w,this);
			openMDIChild(v);
			break;
		case ADD_WAREHOUSE:
			warehouseList.addWarehouseToList(new Warehouse("New_Entry " ,
					"<add>",
					"<add>",
					"<add>",
					"00000",
					0));
			//WarehouseListView v2 = new WarehouseListView("Warehouse List", new WarehouseListController(warehouseList),this);
			//openMDIChild(v2);
			displayChildMessage("New Warehouse Created!");
			
			break;
		case DELETE_WAREHOUSE:
			Warehouse dw = ((WarehouseListView)caller).getSelectedWarehouse();
			warehouseList.removeWarehouseFromList(dw);
			
		
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
			if(children[i] instanceof MDIChildFrame)
				((MDIChildFrame) children[i]).closeChild();
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
	
	public void removeFromOpenViews(MDIChild child) {
		openViews.remove(child);
	}

}
