package photo_renamer;

import GUI.MainViewer;

/**
 * Starts the project.
 * 
 * @author Diadem Shoukralla, Juliaan Posaratnanathan
 *
 */
public class PhotoRenamer {

	/**
	 * This method is for starting and opening the program.
	 * 
	 * @param args
	 *            the command line args.
	 */
	public static void main(String[] args) {
		
		LogList.makeLogList();
		MainViewer.buildWindow().setVisible(true);
	}
}
