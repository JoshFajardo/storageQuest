package controller;

import java.awt.Container;
import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class MDIChild extends JPanel {

	
	protected Container myFrame;
	
	
	protected MDIParent parent;
	
	private String myTitle;
	
	private boolean singleOpenOnly;
	
	private boolean changed;
	private int num=0;
	
	public MDIChild(String title, MDIParent parent){
		this(title);
		setMDIParent(parent);
		myFrame = null;
		singleOpenOnly = false;
	}
	
	public MDIChild(String title){
		myTitle = title;
	}
	
	public void setTitle(String title){
		myTitle = title;
		setInternalFrameTitle(myTitle);
	}
	
	public MDIParent getMasterParent(){
		return parent;
	}
	
	
	public void restoreWindowState() throws PropertyVetoException{
		JInternalFrame curWin = (JInternalFrame) getMDIChildFrame();
		curWin.setIcon(false);
		curWin.moveToFront();
	}
	
	public String getTitle(){
		return myTitle;
	}
	
	public void setMDIParent(MDIParent mf){
		parent = mf;
	}
	
	public boolean isSingleOpenOnly(){
		return singleOpenOnly;
	}
	
	protected JInternalFrame getMDIChildFrame(){
		Container tempContainer = this;
		
		while(!(tempContainer instanceof JInternalFrame) && tempContainer != null)
			tempContainer = tempContainer.getParent();
		if(tempContainer != null)
			return(JInternalFrame) tempContainer;
		return null;
		
	}
	
	private void setInternalFrameTitle(String t){
		if(myFrame == null)
			myFrame = getMDIChildFrame();
		if(myFrame != null)
			((JInternalFrame)myFrame).setTitle(t);
	}
	
	protected void setInternalFrameVisible(boolean v){
		if(myFrame == null)
			myFrame = getMDIChildFrame();
		if(myFrame != null)
			((JInternalFrame) myFrame).setVisible(v);
	}
	
	protected void cleanup(){
		parent.removeFromOpenViews(this);
		System.err.println("MDI child is closing");
	}
	
	public Container getMyFrame(){
		return myFrame;
	}
	
	public void setMyFrame(Container myFrame){
		this.myFrame = myFrame;
	}
	
	public void closeFrame(){
		try{
			((JInternalFrame) myFrame).setClosed(true);
		}catch (PropertyVetoException e){
			parent.displayChildMessage("Error trying to close child");
			
		}
	}
	
	
	public boolean isChanged(){
		return changed;
	}
	
	public void setChanged(boolean changed){
		this.changed = changed;
	}
	
	public boolean saveModel(){
		return true;
	}
	/*
	protected void childClosing(){
		parent.removeFromOpenViews(this);
		
		
		System.err.println("MDIChild closed");
	}*/
	
}
