package views;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

import controller.PartListController;
import controller.MDIChild;
import controller.MDIParent;
import controller.MenuCommands;
import controller.PartListController;
import models.Part;
import models.Warehouse;



public class PartListView extends MDIChild {	

	private JList<Part> listParts;
	private PartListController myList;
	//saves reference to last selected model in JList

	private Part selectedModel;
	
	
	public PartListView(String title, PartListController list, MDIParent m) {
		super(title, m);
		

		list.setMyListView(this);
		

		myList = list;
		listParts = new JList<Part>(myList);
		//allow drag and drop from dog list to person detail view dog list
		//listParts.setDragEnabled(true);
		//listParts.setTransferHandler(new PartDragTransferHandler());
		
		//use our custom cell renderer instead of default (don't want to use Person.toString())
		listParts.setCellRenderer(new PartListCellRenderer());
		listParts.setPreferredSize(new Dimension(200, 200));
		
		//add event handler for double click
		listParts.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				//if double click
		        if(evt.getClickCount() == 2) {
		        	int index = listParts.locationToIndex(evt.getPoint());
		        	//get the part at that index
		        	selectedModel = myList.getElementAt(index);
		        	
		        	//open a new detail view
		        	openDetailView();
		        }
		    }
		});
		
		//add to content pane
		this.add(new JScrollPane(listParts));
		
		//add a Delete button to delete selected part
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JButton button = new JButton("Delete Part");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deletePart();
			}
		});
		panel.add(button);
		
		this.add(panel, BorderLayout.SOUTH);

		this.setPreferredSize(new Dimension(240, 200));
	}


	private void deletePart() {

		int index = listParts.getSelectedIndex();
		if(index < 0)
			return;

		if(index >= myList.getSize())
			return;
		Part p = myList.getElementAt(index);
		if(p == null)
			return;
		selectedModel = p;
		
		//confirm deletion
		String [] options = {"Yes", "No"};
		if(JOptionPane.showOptionDialog(myFrame
				, "Do you want to delete " + p.getPartName() + " ?"
				, "Confirm Deletion"
				, JOptionPane.YES_NO_OPTION
			    , JOptionPane.QUESTION_MESSAGE
			    , null
			    , options
				, options[1]) == JOptionPane.NO_OPTION) {
			return;
		}

		//tell the controller to do the deletion
		parent.doCommand(MenuCommands.DELETE_PART, this);
		
	}
	
	/**
	 * Opens a DogDetailView with the given Dog object
	 */
	public void openDetailView() {
		parent.doCommand(MenuCommands.SHOW_DETAIL_PART, this);
	}
	

	public Part getSelectedPart() {
		return selectedModel;
	}


	@Override
	protected void cleanup() {
		//let superclass do its thing
		super.cleanup();
				
	
		myList.unregisterAsObserver();
	}


	public PartListController getMyList() {
		return myList;
	}

	public void setMyList(PartListController myList) {
		this.myList = myList;
	}

	public JList<Part> getListParts() {
		return listParts;
	}

	public void setListParts(JList<Part> listWarehouse) {
		this.listParts = listWarehouse;
	}

	public Part getSelectedModel() {
		return selectedModel;
	}

	public void setSelectedModel(Part selectedModel) {
		this.selectedModel = selectedModel;
	}
	/*  drag and drop methods, still thinking of using this
	private class DogDragTransferHandler extends TransferHandler {
		private int index = 0;

		public int getSourceActions(JComponent comp) {
	        return COPY_OR_MOVE;
	    }
				
		public Transferable createTransferable(JComponent comp) {
	        index = listDogs.getSelectedIndex();
	        if (index < 0 || index >= myList.getSize()) {
	            return null;
	        }
	        return new TransferableDog( (Dog) listDogs.getSelectedValue());
	    }
	    
	    public void exportDone(JComponent comp, Transferable trans, int action) {
	        if (action != MOVE) {
	            return;
	        }
	    }
	}*/


}
