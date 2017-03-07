package Listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import GUI.Refresher;
import photo_renamer.ImageNode;
import photo_renamer.Log;
import photo_renamer.LogList;

public class RevertNameImageListener implements ActionListener {

	private ImageNode img;
	private JComboBox<String> pastnames;
	private Refresher refresh;

	public RevertNameImageListener(ImageNode img, JComboBox<String> pastnames, Refresher refresh) {
		this.img = img;
		this.pastnames = pastnames;
		this.refresh = refresh;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Log log = LogList.getLog(img, (String) pastnames.getSelectedItem());
		if (log != null) {
			img.revertName(log);
			refresh.refresh();
		}

	}

}
