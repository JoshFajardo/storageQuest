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
		
		MDIMenu menuBar = new MDIMenu (this);
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
			WarehouseListView v1 = new WarehouseListView("Warehouse List", new WarehouseListController(WarehouseList),this);
			
		
		
		
		}
	}
	
	
	
	

}
