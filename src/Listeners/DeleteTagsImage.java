package Listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import GUI.Refresher;
import photo_renamer.ImageNode;
import photo_renamer.TagListSingleton;

public class DeleteTagsImage implements ActionListener{
	private ImageNode img;
	private Refresher refresh;
	
	public DeleteTagsImage(ImageNode img, Refresher refresh) {
		this.img = img;
		this.refresh = refresh; 
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		TagListSingleton.getInstance().removeAllImgTag(img.getPathName());
		refresh.refresh();

	}

}
