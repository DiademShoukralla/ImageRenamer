/**
 * Singleton design pattern
 * 
 * Used because we only want one instance of this class. Any other instances called will be the same.
 *  
 * */

package photo_renamer;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TagListSingleton {

	/**
	 * The static instance of this class
	 */
	private final static TagListSingleton instance = new TagListSingleton();

	/**
	 * The path to the file with the tag information.
	 */
	private final String pathName = "tags.txt";

	/**
	 * A mapping of tags to a set of pathnames that represent an image that has
	 * the tag.
	 */
	private Map<String, HashSet<String>> tagToImg;

	/**
	 * A mapping of pathnames that represent an image to a set of tags that it
	 * has.
	 */
	private Map<String, HashSet<String>> imgToTag;

	/**
	 * Initializes the TagListSingleton. Creates all the data structures from
	 * file.
	 */
	private TagListSingleton() {
		// The file is always going to have same pathname
		File f = new File(pathName);
		try {
			if (f.exists()) {
				readFromFile();
			} else {
				// If file does not exist, create new map
				f.createNewFile();
				tagToImg = new HashMap<String, HashSet<String>>();
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		// Initialize the other map
		makeImgToTag();
	}

	/**
	 * Returns the static instance of this class.
	 */
	public static TagListSingleton getInstance() {
		return instance;
	}

	/**
	 * Initializes the mapping of image pathnames to a set of tags the image
	 * has.
	 */
	private void makeImgToTag() {
		imgToTag = new HashMap<String, HashSet<String>>();
		// Goes through the tagtoimg map to create this map
		for (String tag : tagToImg.keySet()) {
			for (String pathName : tagToImg.get(tag)) {
				if (imgToTag.containsKey(pathName))
					imgToTag.get(pathName).add(tag);
				else {
					HashSet<String> tags = new HashSet<String>();
					tags.add(tag);
					imgToTag.put(pathName, tags);
				}
			}
		}
	}

	/**
	 * Gets the set of tags that the given image has.
	 * 
	 * @param image
	 *            The pathname of the image to find tags for.
	 * @return The set of tags that belong to the image.
	 */
	public HashSet<String> getTags(String image) {
		if (imgToTag.containsKey(image)) {
			//Return a new set so when accessed from another class it cannot be mutated
			HashSet<String> tags = imgToTag.get(image);
			return tags;
		}

		return new HashSet<String>();

	}

	/**
	 * Writes the mapping of tag to image pathnames to the tag file.
	 */
	public void writeToFile() {
		try {
			FileOutputStream file = new FileOutputStream(pathName);
			ObjectOutputStream output = new ObjectOutputStream(file);

			Map<String, HashSet<String>> toSerialize = tagToImg;
			output.writeObject(toSerialize);
			output.close();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads from the tag file to initialize the mapping of tag to image
	 * pathnames.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	private void readFromFile() throws IOException, ClassNotFoundException {
		try {
			InputStream file = new FileInputStream(pathName);

			InputStream buffer = new BufferedInputStream(file);

			ObjectInput input = new ObjectInputStream(buffer);

			tagToImg = (Map<String, HashSet<String>>) input.readObject();

			input.close();
			file.close();
		} catch (EOFException e) {
			tagToImg = new HashMap<String, HashSet<String>>();
		} catch (InvalidClassException e) {
			System.out.println("file doesnt match the type");
		}
	}

	/**
	 * Gets the set of available tags in the program.
	 * 
	 * @return The set of available tags.
	 */
	public Set<String> getTags() {
		Set<String> tags = tagToImg.keySet();
		return tags;
	}

	/**
	 * Updates the tag data structures for a pathname change, as given by the
	 * parameters.
	 * 
	 * @param oldPathName
	 *            The original pathname that's changing.
	 * @param newPathName
	 *            The new pathname thats being implemented.
	 */
	public void changePathNames(String oldPathName, String newPathName) {
		// Iterate though imgToTag map to update tagToImg map
		if(imgToTag.containsKey(oldPathName) && !imgToTag.containsKey(newPathName) && oldPathName != newPathName){
			for (String tag : imgToTag.get(oldPathName)) {
				tagToImg.get(tag).remove(oldPathName);
				tagToImg.get(tag).add(newPathName);
			}
			imgToTag.put(newPathName, imgToTag.get(oldPathName));
			imgToTag.remove(oldPathName);
		}
	}

	/**
	 * Removes a single tag from a single image and updates all the tag data
	 * structures.
	 * 
	 * @param img
	 *            The pathname referring to an image to remove the tag from.
	 * @param tag
	 *            The tag to remove.
	 */
	public void removeTag(String img, String tag) {
		// Check if the image and tag exist in the data structures
		if (imgToTag.containsKey(img) && tagToImg.containsKey(tag)) {
			// If they exist as a pair (i.e are mapped together) we should
			// remove them
			if (tagToImg.get(tag).contains(img)) {
				tagToImg.get(tag).remove(img);
				imgToTag.get(img).remove(tag);
			}
		}
		// If no more images has the tag, remove tag from available list
		if (tagToImg.containsKey(tag))
			if (tagToImg.get(tag).size() == 0)
				tagToImg.remove(tag);
	}

	/**
	 * Removes all the tags in a single image. Updates all the tag data
	 * structures.
	 * 
	 * @param img
	 *            The pathname referring to the image.
	 */
	public void removeAllImgTag(String img) {
		if (imgToTag.containsKey(img))
			// Check if image has tags to remove
			if (!imgToTag.get(img).isEmpty()) {
				HashSet<String> tagsToRemove = new HashSet<String>();
				for (String tag : tagToImg.keySet()) {
					// Going to remove the image from each of the tags image map
					if (tagToImg.get(tag).contains(img))
						tagsToRemove.add(tag);
				}
				// Remove them
				for (String tag : tagsToRemove)
					removeTag(img, tag);
			}
	}

	/**
	 * 
	 * Adds a single tag to a single image. Updates all the tag data structures. Tag should not contain "@".
	 * 
	 * @param img
	 *            The pathname referring to the image the tag is to be added to.
	 * @param tag
	 *            The tag to be added.
	 */
	public void addTag(String img, String tag) {
		if(!tag.contains("@")){
			// Check this is a new tag or image in the data structures
			if (!imgToTag.containsKey(img))
				imgToTag.put(img, new HashSet<String>());
			if (!tagToImg.containsKey(tag)) {
				tagToImg.put(tag, new HashSet<String>());
			}
			//Add them, will not duplicate because they are sets
			imgToTag.get(img).add(tag);
			tagToImg.get(tag).add(img);
		}
	}

	/**
	 * Returns all the tags for all images (formatted as a string).
	 * 
	 * @return A string representation of all the tags.
	 */
	@Override
	public String toString() {
		String tagString = "";
		for (String tag : tagToImg.keySet())
			tagString += tag + "\n";

		return tagString;
	}
	
}
