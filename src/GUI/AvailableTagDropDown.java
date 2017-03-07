/**
 * An observer that observes the refresher (following the observer design pattern). 
 * Extends JComboBox and will refresh it on the screen when notified from observable.
 * */

package GUI;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import photo_renamer.TagListSingleton;

public class AvailableTagDropDown extends JComboBox<String> implements Observer{

	private static final long serialVersionUID = 1L;
	
	
	public AvailableTagDropDown() {
		super();
		
		super.setModel(getNewModel());
		super.setPreferredSize(new Dimension(180, 25));
		super.setVisible(true);
		super.setEditable(true);

		
	}
	
	private DefaultComboBoxModel<String> getNewModel(){
		Set<String> allTagSet = TagListSingleton.getInstance().getTags();
		String[] allTagList= allTagSet.toArray(new String[allTagSet.size()]) ;
		return new DefaultComboBoxModel<String>(allTagList);
	}
	
	@Override 
	public void update(Observable o, Object arg) {
		super.setModel(getNewModel());
		super.repaint();
		super.revalidate();
		
	}

}
