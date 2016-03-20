package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MDIMenu extends JMenuBar {
	
	private MDIParent parent;
	
	public MDIMenu(MDIParent w) {
		super();
		
		this.parent = w;
		
		JMenu menu = new JMenu("File");
		JMenuItem menuItem = new JMenuItem("Quit");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.doCommand(MenuCommands.APP_QUIT, null);
			}
		});
		menu.add(menuItem);
		this.add(menu);	
		
		
		menu = new JMenu("Warehouses");
		menuItem = new JMenuItem("Show Warehouse List");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.doCommand(MenuCommands.SHOW_LIST_WAREHOUSE, null);
			}
		});
		menu.add(menuItem);
		this.add(menu);		
		
		menu.add(menuItem);

		menuItem = new JMenuItem("Add Warehouse");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.doCommand(MenuCommands.ADD_WAREHOUSE, null);
			}
		});
		menu.add(menuItem);
		this.add(menu);	
		
		
		menu = new JMenu("Parts");
		menuItem = new JMenuItem("Part List");
		menuItem.addActionListener(new ActionListener (){
			@Override
			public void actionPerformed(ActionEvent e){
				parent.doCommand(MenuCommands.SHOW_LIST_PARTS, null);
			}
		});
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Add Part");
		menuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				parent.doCommand(MenuCommands.ADD_PART, null);
			}
		});
		
		menu.add(menuItem);
		this.add(menu);
		
		
	}
}
