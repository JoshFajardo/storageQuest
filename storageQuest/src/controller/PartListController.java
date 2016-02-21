package controller;

import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractListModel;

import models.Part;
import models.PartList;

public class PartListController extends AbstractListModel<Part> implements Observer {

	private PartList myList;
	
	private MDIChild myListView;
	
	public PartListController(PartList pl){
		super();
		myList = pl;
		
		pl.addObserver(this);
	}
	
	@Override
	public int getSize(){
		return myList.getList().size();
	}
	
	@Override
	public Part getElementAt (int index){
		if(index >= getSize())
			throw new IndexOutOfBoundsException("index "+ index + " is out of the list bounds");
		return myList.getList().get(index);
	}
	
	public MDIChild getMyListView(){
		return myListView;
	}
	
	public void setMyListView(MDIChild myListView){
		this.myListView = myListView;
	}
	
	public void unregisterAsObserver(){
		myList.deleteObserver(this);
	}
	
	@Override
	public void update(Observable o, Object arg){
		
		fireContentsChanged(this, 0, getSize());
		myListView.repaint();
	}
	
	
}
