/**
 * The observable in the Observer design pattern. This class knows when all the other components 
 * (the observers) should be refreshed on the screen and thus notifies them.
 * 
 * */

package GUI;

import java.util.Observable;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Refresher extends Observable {

	JPanel toRefresh;
	JComboBox<String> allTagDropDown;
	JComboBox<String> tagDropDown;
	JComboBox<String> nameDropDown;
	JLabel label;

	public Refresher(JPanel toRefresh) {
		super();
		this.toRefresh = toRefresh;
	}

	// Notifies the observers of the change
	public void refresh() {
		setChanged();
		notifyObservers();

		toRefresh.repaint();
		toRefresh.revalidate();
	}

}
