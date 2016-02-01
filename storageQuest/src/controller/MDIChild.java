package controller;

import java.awt.Container;
import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class MDIChild extends JPanel {

	
	private Container myFrame;
	
	
	protected MDIParent parent;
	
	private String myTitle;
	
	private boolean singleOpenOnly;
	
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
	
	protected void setInternalFrameVisiable(boolean v){
		if(myFrame == null)
			myFrame = getMDIChildFrame();
		if(myFrame != null)
			((JInternalFrame) myFrame).setVisible(v);
	}
	
	protected void childClosing(){
		parent.removeFromOpenViews(this);
		
		
		System.err.println("MDIChild closed");
	}
	
}
