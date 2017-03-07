package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;

import Listeners.FileChooserButtonListener;
import Listeners.ViewLogListener;
import Listeners.quitListener;
import Listeners.viewTagsListener;

/**
 * Create and show a directory explorer, which displays the contents of a
 * directory.
 */
public class MainViewer {

	/**
	 * Create and return the window for the directory explorer.
	 *
	 * @return the window for the directory explorer
	 */
	public static JFrame buildWindow() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		// Big frame label
		JLabel directoryLabel = new JLabel("Please Choose a Directory");

		// Big frame
		JFrame directoryFrame = new JFrame("Photo Renamer!!");
		directoryFrame.setSize(1100, 700);
		directoryFrame.setResizable(false);

		// Contains the two sides
		JPanel screen = new JPanel();
		screen.setLayout(new BoxLayout(screen, BoxLayout.LINE_AXIS));

		// List the directories here
		JPanel leftSide = new JPanel();
		leftSide.setLayout(new BoxLayout(leftSide, BoxLayout.PAGE_AXIS));

		leftSide.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
				"Directories"));

		JScrollPane scrollerLeft = new JScrollPane(leftSide);
		scrollerLeft.setPreferredSize(new Dimension(2, 2));
		scrollerLeft.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);
		screen.add(scrollerLeft);


		JPanel rightSide = makeRightSide();
		// log Panel

		JScrollPane scrollerRight = new JScrollPane(rightSide);
		scrollerRight.setPreferredSize(new Dimension(1, 2));
		scrollerRight.setAlignmentX(JScrollPane.RIGHT_ALIGNMENT);
		screen.add(scrollerRight);

		// The directory choosing button.
		JButton openButton = new JButton("Choose Directory");
		openButton.setVerticalTextPosition(AbstractButton.CENTER);
		openButton.setHorizontalTextPosition(AbstractButton.LEADING);
		openButton.setMnemonic(KeyEvent.VK_D);
		openButton.setActionCommand("disable");

		// The listener for openButton.
		ActionListener buttonListener = new FileChooserButtonListener(directoryFrame, leftSide, directoryLabel,
				fileChooser);
		openButton.addActionListener(buttonListener);

		//exitpanel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		//exit button
		JButton exitButton = new JButton("quit");
		exitButton.setVerticalTextPosition(AbstractButton.CENTER);
		exitButton.setHorizontalTextPosition(AbstractButton.LEADING);
		exitButton.addActionListener(new quitListener());
		
		topPanel.add(directoryLabel, BorderLayout.WEST);
		topPanel.add(exitButton,BorderLayout.EAST);
		

		// Put it all together.
		Container c = directoryFrame.getContentPane();
		c.add(topPanel, BorderLayout.PAGE_START);
		c.add(openButton, BorderLayout.PAGE_END);
		c.add(screen);
		directoryFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		return directoryFrame;
	}


	public static JPanel makeRightSide() {
		// List the options here
		JPanel rightSide = new JPanel();
		rightSide.setLayout(new BoxLayout(rightSide, BoxLayout.PAGE_AXIS));
		rightSide.setBackground(Color.lightGray);
		rightSide.add(Box.createRigidArea(new Dimension(0, 50)));

		makeLogPanel(rightSide);

		return rightSide;
	}

	private static void makeLogPanel(JPanel rightSide) {
		JPanel logPanel = new JPanel(new FlowLayout());
		logPanel.setOpaque(false);
		rightSide.add(logPanel);

		JButton viewLogs = new JButton("View Logs");
		viewLogs.setPreferredSize(new Dimension(140, 25));
		viewLogs.setVerticalTextPosition(AbstractButton.CENTER);
		viewLogs.setHorizontalTextPosition(AbstractButton.LEADING);
		logPanel.add(viewLogs);

		// add action listener
		// Set up the area for displaying log and tag contents.
		JTextArea textArea = new JTextArea(20, 1);
		textArea.setEditable(false);

		// Put it in a scroll pane in case the output is long.
		JScrollPane scrollPane = new JScrollPane(textArea);
		rightSide.add(scrollPane);
		rightSide.add(Box.createRigidArea(new Dimension(0, 25)));

		ViewLogListener viewLogListener = new ViewLogListener(textArea);
		viewLogs.addActionListener(viewLogListener);

		logPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		
		//add button to show tags in text area
		
		JButton viewTags = new JButton("View Tags");
		viewTags.setPreferredSize(new Dimension(140, 25));
		viewTags.setVerticalTextPosition(AbstractButton.CENTER);
		viewTags.setHorizontalTextPosition(AbstractButton.LEADING);
		logPanel.add(viewTags);
		
		viewTagsListener viewTagListener = new viewTagsListener(textArea);
		viewTags.addActionListener(viewTagListener);

	}
}