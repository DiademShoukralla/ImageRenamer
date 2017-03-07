package Listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;

import photo_renamer.LogList;

public class ViewLogListener implements ActionListener{
	private JTextArea textArea;
	
	

	public ViewLogListener(JTextArea textArea) {
		this.textArea = textArea;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		textArea.setText(LogList.viewLogs());
	}

}
