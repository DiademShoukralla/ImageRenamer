package Listeners;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import photo_renamer.FileNode;
import photo_renamer.FileType;
import photo_renamer.ImageNode;

/**
 * The listener for the button to choose a directory. This is where most of the
 * work is done.
 */
public class FileChooserButtonListener implements ActionListener {

	/** The window the button is in. */
	private JPanel mainPanel;
	/** The label for the full path to the chosen directory. */
	private JLabel directoryLabel;
	/** The file chooser to use when the user clicks. */
	private JFileChooser fileChooser;

	private JFrame frame;

	/** The area to use to display the nested directory contents. */

	/**
	 * An action listener for window dirFrame, displaying a file path on
	 * dirLabel, using fileChooser to choose a file.
	 *
	 * @param dirFrame
	 *            the main window
	 * @param dirLabel
	 *            the label for the directory path
	 * @param fileChooser
	 *            the file chooser to use
	 */
	public FileChooserButtonListener(JFrame frame, JPanel dirFrame, JLabel dirLabel, JFileChooser fileChooser) {
		this.mainPanel = dirFrame;
		this.directoryLabel = dirLabel;
		this.frame = frame;
		this.fileChooser = fileChooser;
	}

	/**
	 * Handle the user clicking on the open button.
	 *
	 * @param e
	 *            the event object
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		int returnVal = fileChooser.showOpenDialog(frame.getContentPane());

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			if (file.exists()) {
				directoryLabel.setText("Selected File" + file.getAbsolutePath());

				// Make the root.
				FileNode fileTree = new FileNode(file.getName(), null, FileType.DIRECTORY, file.getAbsolutePath());
				FileNode.buildTree(file, fileTree);

				buildDirectoryContents(fileTree);

			}
		} else {
			directoryLabel.setText("No Path Selected");
		}
	}
	// Only called called on directories
		private void buildDirectoryContents(FileNode fileTree) {
			JPanel direcPanel = new JPanel();
			direcPanel.setLayout(new BoxLayout(direcPanel, BoxLayout.PAGE_AXIS));
			direcPanel.add(new JLabel(fileTree.getPathName()));
			mainPanel.add(direcPanel);
			mainPanel.add(Box.createRigidArea(new Dimension(7, 15)));

			for (FileNode child : fileTree.getChildren()) {
				if (child.isDirectory()) {
					buildDirectoryContents(child);
				} else {
					File imgFile = new File(child.getPathName());
					Image imgImage;
					try {
						imgImage = ImageIO.read(imgFile).getScaledInstance(35, 35, Image.SCALE_SMOOTH);
						ImageIcon icon = new ImageIcon(imgImage);
						JLabel label = new JLabel(((ImageNode)child).getTagName(), icon, SwingConstants.RIGHT);
						MouseListener iconClick = new IconChooserMouseListener((ImageNode) child);
						label.addMouseListener(iconClick);
						direcPanel.add(label);
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}

		}
	

}
