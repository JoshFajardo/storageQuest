package controller;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

public class MDIChildFrame extends JInternalFrame implements InternalFrameListener {

	
	protected MDIChild myChild;
	
	
	
	public MDIChildFrame(String title, boolean resizable, boolean closable, boolean maximizable,
			boolean iconifiable, MDIChild child){
		super(title, resizable, closable, maximizable,iconifiable);
		myChild = child;
		this.add(child,BorderLayout.CENTER);
		
		this.addInternalFrameListener(this);
	}
	
	@Override
	public void internalFrameOpened(InternalFrameEvent e){
		
	}
	public void closeChild(){
		myChild.childClosing();
	}
	@Override
	public void internalFrameClosing(InternalFrameEvent e) {
		// tell child to clean up (e.g., remove from MDIParent's openViews list
		closeChild();
	}

	@Override
	public void internalFrameClosed(InternalFrameEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void internalFrameIconified(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void internalFrameDeiconified(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void internalFrameActivated(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void internalFrameDeactivated(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
