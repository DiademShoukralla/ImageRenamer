/**
 * This class sets up the Observer design pattern. 
 * 
 * The combo boxes and frame label are the observers. 
 * The observable is the refresh class.
 * The buttons trigger the update in the refresh class
 * 
 * */

package GUI;

import Listeners.AddTagListener;
import Listeners.DeleteOneTagImageListener;
import Listeners.DeleteTagsImage;
import Listeners.OpenDirectoryListener;
import Listeners.RevertNameImageListener;
import photo_renamer.ImageNode;
import Listeners.RenameListener;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
/**
 * @author Diadem Shourkalla, Juliaan Posaratnatnathan
 * */
public class ImageViewer {

	private ImageNode image;

	private Refresher refreshObserveable;
	
	private ImageLabel frameLabel;

	// Combo boxes
	private AvailableTagDropDown allTagDropDown;
	private TagDropDown tagDropDown;
	private NameDropDown nameDropDown;

	// buttons
	private JButton addTag;
	private JButton deleteTag;
	private JButton deleteAllTags;
	private JButton rename;
	private JButton revert;
	private JButton open;

	public ImageViewer(ImageNode img) {
		this.image = img;
	}

	public JFrame buildWindow() {

		JFrame viewer = new JFrame("Image Viewer");
		viewer.setBounds(250, 250, 1000, 700);
		viewer.setResizable(false);

		viewer.add(makeMainPanel());

		return viewer;
	}

	public JPanel makeMainPanel() {
		JPanel mainPanel = new JPanel(); // border layout
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

		frameLabel = new ImageLabel(image);
		frameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		mainPanel.add(frameLabel);

		File imgFile = new File(image.getPathName());
		try {
			Image imgImage = ImageIO.read(imgFile).getScaledInstance(-1, 400, Image.SCALE_SMOOTH);
			JLabel pic = new JLabel(new ImageIcon(imgImage));
			pic.setAlignmentX(Component.CENTER_ALIGNMENT);
			mainPanel.add(pic);
		} catch (IOException e) {
			e.printStackTrace();
		}

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		makeDeletePanel(mainPanel); 
		makeAddPanel(mainPanel); 
		makeRevertPanel(mainPanel); 
		makeOtherPanel(mainPanel); 

		
		//add observers
		addObservers(mainPanel);
		
		// action listeners
		addActionListenersToAll();

		return mainPanel;

	}
	
	private void addObservers(JPanel mainPanel){
		refreshObserveable = new Refresher(mainPanel);
		refreshObserveable.addObserver(tagDropDown);
		refreshObserveable.addObserver(allTagDropDown);
		refreshObserveable.addObserver(nameDropDown);
		refreshObserveable.addObserver(frameLabel);

	}
	

	private void addActionListenersToAll() {

		// action listener for delete
		DeleteOneTagImageListener deleteTagButtonListener = new DeleteOneTagImageListener(image, tagDropDown, refreshObserveable);
		deleteTag.addActionListener(deleteTagButtonListener);

		// action listener for delete all
		DeleteTagsImage deleteAllTagButtonListener = new DeleteTagsImage(image, refreshObserveable);
		deleteAllTags.addActionListener(deleteAllTagButtonListener);

		// action listener for add tag
		AddTagListener addTagButtonListener = new AddTagListener(image, allTagDropDown, refreshObserveable);
		addTag.addActionListener(addTagButtonListener);

		// action listener for rename
		RenameListener renameButtonListener = new RenameListener(image, refreshObserveable);
		rename.addActionListener(renameButtonListener);

		// add action listener for revert
		RevertNameImageListener revertListener = new RevertNameImageListener(image, nameDropDown, refreshObserveable);
		revert.addActionListener(revertListener);

		// addActionlistener
		OpenDirectoryListener openListener = new OpenDirectoryListener(image);
		open.addActionListener(openListener);
	}

	private void makeDeletePanel(JPanel panel) {
		// Delete tag panel
		JPanel deleteTagPanel = new JPanel(new FlowLayout());
		panel.add(deleteTagPanel);

		// Picture's tags
		tagDropDown = new TagDropDown(image);
		deleteTagPanel.add(tagDropDown);

		deleteTagPanel.add(Box.createRigidArea(new Dimension(10, 0)));

		// delete a specific tag button
		deleteTag = new JButton("Delete This Tag");
		deleteTag.setPreferredSize(new Dimension(180, 25));
		deleteTag.setVerticalTextPosition(AbstractButton.CENTER);
		deleteTag.setHorizontalTextPosition(AbstractButton.LEADING);
		deleteTagPanel.add(deleteTag);

		deleteTagPanel.add(Box.createRigidArea(new Dimension(10, 0)));

		// Delete all of this image's tags
		deleteAllTags = new JButton("Delete All Tags");
		deleteAllTags.setPreferredSize(new Dimension(180, 25));
		deleteAllTags.setVerticalTextPosition(AbstractButton.CENTER);
		deleteAllTags.setHorizontalTextPosition(AbstractButton.LEADING);
		deleteTagPanel.add(deleteAllTags);

	}

	// add panel
	private void makeAddPanel(JPanel mainPanel) {
		// add panel
		JPanel addPanel = new JPanel(new FlowLayout());
		mainPanel.add(addPanel);

		allTagDropDown = new AvailableTagDropDown();
		addPanel.add(allTagDropDown);

		addPanel.add(Box.createRigidArea(new Dimension(20, 0)));

		// add button
		addTag = new JButton("Add Tag");
		addTag.setPreferredSize(new Dimension(180, 25));
		addTag.setVerticalTextPosition(AbstractButton.CENTER);
		addTag.setHorizontalTextPosition(AbstractButton.LEADING);
		addPanel.add(addTag);

		addPanel.add(Box.createRigidArea(new Dimension(20, 0)));
		
		// rename button
		// add button
		rename = new JButton("Rename Image");
		rename.setPreferredSize(new Dimension(180, 25));
		rename.setVerticalTextPosition(AbstractButton.CENTER);
		rename.setHorizontalTextPosition(AbstractButton.LEADING);
		addPanel.add(rename);

	}

	// revert panel
	private void makeRevertPanel(JPanel mainPanel) {

		// revert panel
		JPanel revertPanel = new JPanel(new FlowLayout());
		mainPanel.add(revertPanel);

		// revert dropdown
		nameDropDown = new NameDropDown(image); 
		revertPanel.add(nameDropDown);

		revertPanel.add(Box.createRigidArea(new Dimension(20, 0)));

		// revert button
		revert = new JButton("Revert");
		revert.setPreferredSize(new Dimension(180, 25));
		revert.setVerticalTextPosition(AbstractButton.CENTER);
		revert.setHorizontalTextPosition(AbstractButton.LEADING);
		revertPanel.add(revert);

	}

	// other panel
	private void makeOtherPanel(JPanel main) {
		// last panel
		JPanel lastPanel = new JPanel();
		main.add(lastPanel);

		// open in directory button
		open = new JButton("Open in OS");
		open.setPreferredSize(new Dimension(180, 25));
		open.setVerticalTextPosition(AbstractButton.CENTER);
		open.setHorizontalTextPosition(AbstractButton.LEADING);
		lastPanel.add(open);

	}
}
