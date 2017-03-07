package photo_renamer;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

/**
 * 
 * A leaf of a tree representing a directory structure where all leaves
 * represent image files.
 * 
 * @author Diadem Shoukralla, Juliaan Posaratnanathan
 *
 */
public class ImageNode extends FileNode implements Serializable {

	/**
	 * ImageNode class's serial number
	 */
	private static final long serialVersionUID = 8547936008152442123L;

	/**
	 * The node's name with it's tags.
	 */
	private String tagName;

	/**
	 * Initializes a generic imagenode.
	 */
	public ImageNode() {
		super();
	}

	/**
	 * An imagenode in the tree.
	 * 
	 * @param name
	 *            The file
	 * @param parent
	 *            The parent of this imagenode.
	 * @param type
	 *            They type of the file.
	 * @param pathName
	 *            The pathname of the file this imagenode represents.
	 * 
	 * @see FileType
	 */
	public ImageNode(String name, FileNode parent, FileType type, String pathName) {
		super(name.split("@")[0].trim(), parent, type, pathName);
		tagName = name;
	}

	/**
	 * Gets tagname of this imagenode.
	 * 
	 * @return The name of this imagenode with its tags.
	 */
	public String getTagName() {
		return tagName;
	}

	/**
	 * Updates this imagenode's tagName to include its current tags. Updates the
	 * change in the OS's pathname and in TagList and LogList.
	 * 
	 */
	public void newName() {
		// The image keeps track of its name without the tags (getName()), and
		// we are updating the name with tags by parsing through the tags it
		// currently has
		String newName = getName();
		for (String tag : TagListSingleton.getInstance().getTags(this.getPathName()))
			newName += " @" + tag;
		
		// If the image's current name with tags is different from what it was before, change name.
		if (!tagName.equals(newName)) {
			//Make the current os' file name the new one
			File oldNameFile = new File(getPathName());
			File newNameFile = new File(getParent().getPathName() + "/" + newName);
			oldNameFile.renameTo(newNameFile);
			
			//Log this change
			Log newLog = new Log(new Timestamp(Calendar.getInstance().getTime().getTime()), newName, tagName,
					this.getPathName());
			LogList.addLog(newLog);

			//Update this change in all the logs and tag managers
			TagListSingleton.getInstance().changePathNames(oldNameFile.getAbsolutePath(), newNameFile.getAbsolutePath());
			LogList.changePathName(oldNameFile.getAbsolutePath(), newNameFile.getAbsolutePath());
			setPathName(newNameFile.getAbsolutePath());
			tagName = newName;
		}

	}

	/**
	 * Precondition: A tag cannot include "@".
	 * 
	 * Gets the set of tags that refers to the name of a file referring to an
	 * image.
	 * 
	 * @param name
	 *            The name of a file referring to some image.
	 * @return A set of tags from the given image name.
	 */
	public HashSet<String> convertToTags(String name) {
		// returns the tags its supposed to have
		String betterName = name.replace(getName(), "").trim().replaceFirst("@", "");
		List<String> help = Arrays.asList(betterName.replaceAll(" @", "@").trim().split("@"));
		ArrayList<String> badTags = new ArrayList<String>(help.size());
		badTags.addAll(help);
		HashSet<String> toRemove = new HashSet<String>();
		for (String tag : badTags)
			if (tag.equals(""))
				toRemove.add(tag);
		for (String tag : toRemove)
			badTags.remove(tag);
		HashSet<String> newTags = new HashSet<String>(badTags);
		return newTags;
	}

	/**
	 * Reverts the name of this imagenode to the oldname of the given log and
	 * updates the TagList.
	 * 
	 * @param log
	 *            The log containing the name to revert to
	 * @see Log, TagList
	 */
	public void revertName(Log log) {
		if (LogList.getLogImg(this).contains(log)) {
			TagListSingleton.getInstance().removeAllImgTag(this.getPathName());
			HashSet<String> newTags = convertToTags(log.getOldName());
			for (String tag : newTags)
				TagListSingleton.getInstance().addTag(this.getPathName(), tag);
			newName();
		}
	}

	/**
	 * Opens the image that this imagenode refers to in the OS's file explorer.
	 * 
	 */
	public void openDirectory() {
		try {
			Desktop.getDesktop().open(new File(this.getParent().getPathName()));
		} catch (FileNotFoundException e) {
			System.out.println("Sorry file doesnt exist u scrub ://");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
