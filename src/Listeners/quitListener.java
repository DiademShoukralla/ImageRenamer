package Listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import photo_renamer.LogList;
import photo_renamer.TagListSingleton;

public class quitListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		if(!TagListSingleton.getInstance().getTags().isEmpty())
			TagListSingleton.getInstance().writeToFile();
		if(!LogList.getLogList().isEmpty())
			LogList.writeToFile();
		System.exit(0);
	}

}
