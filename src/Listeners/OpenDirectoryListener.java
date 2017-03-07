package Listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import photo_renamer.ImageNode;

public class OpenDirectoryListener implements ActionListener{
	
	private ImageNode img; 
	
	

	public OpenDirectoryListener(ImageNode img) {
		this.img = img;
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		img.openDirectory();
	}

}
