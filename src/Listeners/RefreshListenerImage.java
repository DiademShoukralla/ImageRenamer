package Listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import GUI.ImageViewer;

public class RefreshListenerImage implements ActionListener{

	private JPanel toRefresh;
	private ImageViewer viewer;

	public RefreshListenerImage(JPanel toRefresh,ImageViewer viewer) {
		this.toRefresh = toRefresh;
		this.viewer = viewer;
	}



	@Override
	public void actionPerformed(ActionEvent e) {		
		JPanel parentPanel = (JPanel) (SwingUtilities.getUnwrappedParent(toRefresh));
		parentPanel.removeAll();
		JPanel newPanel = viewer.makeMainPanel();

		parentPanel.add(newPanel);
		parentPanel.repaint();
		parentPanel.revalidate();
	}



}
