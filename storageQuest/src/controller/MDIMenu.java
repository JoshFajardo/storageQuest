package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import login.User;
import login.Verify;
import login.Session;
import login.ABACPolicy;
import login.SecurityException;

public class MDIMenu extends JMenuBar {
	
	private MDIParent parent;
	private int id=0;
	Verify u = new Verify();
	ABACPolicy pol = new ABACPolicy();
	
	public boolean checkPerm(String user, String check){
		if(pol.canUserAccessFunction(user, check)==true)
			return true;
		return false;
	}
	
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
		
		
		
		
		menu = new JMenu("Login");
		menuItem = new JMenuItem("login as bob");
		menuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				User user = new User("bob", "123456", "Boberino");
				
				try {
					id = u.login(user.getLogin(), user.getPasswordHash());
					parent.displayChildMessage("Hello bob ");
					parent.getUserName(user.getLogin());
					
				} catch (SecurityException e1) {
					//e.printStackTrace();
					parent.displayChildMessage("Unable to login as bob");
				}
				//parent.doCommand(MenuCommands.LOGIN_AS_BOB, null);
			}
		});
		menu.add(menuItem);
		this.add(menu);
		
		menuItem = new JMenuItem("login as Sue");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				User user = new User("sue", "password", "pur-sue");
				
				try {
					id = u.login(user.getLogin(), user.getPasswordHash());
					parent.displayChildMessage("Hello sue ");
					parent.getUserName(user.getLogin());
					
				} catch (SecurityException e1) {
					//e.printStackTrace();
					parent.displayChildMessage("Unable to login as sue");
				}
				//parent.doCommand(MenuCommands.LOGIN_AS_SUE, null);
			}
		});
		menu.add(menuItem);
		this.add(menu);
		
		menuItem = new JMenuItem("login as Ragnar");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				User user = new User("ragnar","viking4life", "ragBrok");
				
				try {
					id = u.login(user.getLogin(), user.getPasswordHash());
					parent.displayChildMessage("Hello ragnar ");
					parent.getUserName(user.getLogin());
					
				} catch (SecurityException e1) {
					//e.printStackTrace();
					parent.displayChildMessage("Unable to login as ragnar");
				}
				//parent.doCommand(MenuCommands.LOGIN_AS_RAGNAR, null);
			}
		});
		menu.add(menuItem);
		this.add(menu);
		
		menuItem = new JMenuItem("login as Zeratul");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				User user = new User("zeratul","Shakuras", "zer");
				
				try {
					id = u.login(user.getLogin(), user.getPasswordHash());
					parent.displayChildMessage("Hello zeratul ");
					parent.getUserName("guest");
					
				} catch (SecurityException e1) {
					//e.printStackTrace();
					parent.displayChildMessage("Unable to login as zeratul");
				}
				//parent.doCommand(MenuCommands.LOGIN_AS_ZERATUL, null);
			}
		});
		menu.add(menuItem);
		this.add(menu);
		
		menuItem = new JMenuItem("Logout");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				u.logout(id);
				parent.displayChildMessage("logged out");
				parent.getUserName("guest");
				parent.doCommand(MenuCommands.LOGOUT, null);
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
