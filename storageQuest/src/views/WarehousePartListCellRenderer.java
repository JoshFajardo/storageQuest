package views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import models.Part;
import models.WarehousePart;

public class WarehousePartListCellRenderer implements ListCellRenderer<WarehousePart> {

	private final DefaultListCellRenderer DEFAULT_RENDERER = new DefaultListCellRenderer();
	

	private JButton button;
	
	@Override
	public Component getListCellRendererComponent(JList<? extends WarehousePart> list, WarehousePart value, int index,
			boolean isSelected, boolean cellHasFocus) {

		JLabel renderer = (JLabel) DEFAULT_RENDERER.getListCellRendererComponent(list, value.getPart().getPartName(), index, isSelected, cellHasFocus);
		JPanel rowRenderer = new JPanel();


		rowRenderer.setLayout(new BorderLayout());
		rowRenderer.add(renderer, BorderLayout.CENTER);

		//delete button for every row
		button = new JButton("X");
		button.setPreferredSize(new Dimension(20, 20));
		rowRenderer.add(button, BorderLayout.EAST);
		return rowRenderer;
	}

	public boolean mouseOnButton(MouseEvent event) {
		Point pt = event.getPoint();
		if(pt.getX() >= button.getX() && pt.getX() <= button.getX() + button.getWidth()) {
				return true;
		
		}
		return false;
	}
}

