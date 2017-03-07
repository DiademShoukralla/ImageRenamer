package Listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import GUI.ImageViewer;
import photo_renamer.ImageNode;


public class IconChooserMouseListener implements MouseListener{
	
	private ImageNode image;

	/**
	 * An action listener for window dirFrame, displaying a file path on
	 * dirLabel, using fileChooser to choose a file.
	 *
	 * @param dirFrame
	 *            the main window
	 * @param dirLabel
	 *            the label for the directory path
	 * @param fileChooser
	 *            the file chooser to use
	 */
	public IconChooserMouseListener(ImageNode image) {
		this.image = image;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {

		ImageViewer viewer = new ImageViewer(image);
		viewer.buildWindow().setVisible(true);
	}

	
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}
	
	//Don't care
	@Override
	public void mousePressed(MouseEvent e) {
		
	}
	//Don't care
	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	//Don't care
	@Override
	public void mouseExited(MouseEvent e) {
		
	}

}
