package Listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import GUI.Refresher;
import photo_renamer.ImageNode;
import photo_renamer.TagListSingleton;

public class DeleteOneTagImageListener implements ActionListener{
	
	private JComboBox<String> dropDownImgTag;
	private ImageNode img;
	private Refresher refresh;
	
	public DeleteOneTagImageListener(ImageNode img, JComboBox<String> dropDownImgTag, Refresher refresh){
		this.dropDownImgTag = dropDownImgTag;
		this.img = img;
		this.refresh = refresh;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String tag = (String)dropDownImgTag.getSelectedItem();
		TagListSingleton.getInstance().removeTag(img.getPathName(), tag);
		refresh.refresh();
	
	}

}
