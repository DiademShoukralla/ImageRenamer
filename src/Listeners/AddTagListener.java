package Listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import GUI.Refresher;
import photo_renamer.ImageNode;
import photo_renamer.TagListSingleton;

public class AddTagListener implements ActionListener {

	private JComboBox<String> dropDown;
	private ImageNode img;
	private Refresher refresh;

	public AddTagListener(ImageNode img, JComboBox<String> dropDownAllTags, Refresher refresh) {
		this.img = img;
		this.dropDown = dropDownAllTags;
		this.refresh = refresh;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String tag = (String) dropDown.getSelectedItem();
		if (tag != null) {
			if (!tag.trim().equals("")) {
				tag = tag.trim();
				TagListSingleton.getInstance().addTag(img.getPathName(), tag);
				refresh.refresh();
			}
		}
	}

}
