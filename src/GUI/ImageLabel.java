
/**
 * An observer that observes the refresher (following the observer design pattern). 
 * Extends JLabel and will refresh it on the screen when notified from observable.
 * */
package GUI;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;

import photo_renamer.ImageNode;

public class ImageLabel extends JLabel implements Observer {

	private static final long serialVersionUID = 642567690493614501L;

	private ImageNode image;

	public ImageLabel(ImageNode image) {
		super();
		this.image = image;
		super.setText(getNewLabel());
	}

	private String getNewLabel(){
		return image.getTagName();
	}

	@Override
	public void update(Observable o, Object arg) {
		super.setText(getNewLabel());
		super.repaint();
		super.revalidate();

	}

}
