package Listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import GUI.Refresher;
import photo_renamer.ImageNode;

public class RenameListener implements ActionListener {

	private ImageNode img;
	private Refresher refresh;
	
	public RenameListener(ImageNode img, Refresher refresh){
		this.img = img;
		this.refresh = refresh;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		img.newName();
		refresh.refresh();
	}

}
