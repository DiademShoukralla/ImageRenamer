package photo_renamer;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;


/**
 * @author Diadem Shoukralla, Juliaan Posaratnanathan
 *
 */

public class TestTagListSingleton {

	// Object being tested
	/** The class being tested.  */
	private TagListSingleton taglist;

	// Declare methods

	/** The reflected method that represents TagListSingleton.getInstance */
	private Method getInstance;

	/** The reflected method that represents TagListSingleton.getTags for an image */
	private Method getTagsImage;

	/** The reflected method that represents TagListSingleton.getTags for all images */
	private Method getTagsAll;

	/** The reflected method that represents TagListSingleton.changePathNames for an image */
	private Method changePathNames;

	/** The reflected method that represents TagListSingleton.removeTag */
	private Method removeTag;
	
	/** The reflected method that represents TagListSingleton.removeAllImgTag */
	private Method removeAllImgTag;

	/** The reflected method that represents TagListSingleton.addTag */
	private Method addTag;

	/** The reflected method that represents TagListSingleton.toString */
	private Method toString;

	// Declare fields

	/** The reflected field that represents TagListSingleton.tagToImg */
	private Field tagToImg;

	/** The reflected field that represents TagListSingleton.imgToTag */
	private Field imgToTag;

	@Before
	public void setUp() throws NoSuchMethodException, SecurityException, NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		// Initialize taglist
		taglist = TagListSingleton.getInstance();

		// Initialize the methods
		getInstance = TagListSingleton.class.getDeclaredMethod("getInstance");
		getTagsImage = TagListSingleton.class.getDeclaredMethod("getTags", String.class);
		getTagsAll = TagListSingleton.class.getDeclaredMethod("getTags");
		changePathNames = TagListSingleton.class.getDeclaredMethod("changePathNames", String.class, String.class);
		removeTag = TagListSingleton.class.getDeclaredMethod("removeTag", String.class, String.class);
		removeAllImgTag = TagListSingleton.class.getDeclaredMethod("removeAllImgTag", String.class);
		addTag = TagListSingleton.class.getDeclaredMethod("addTag", String.class, String.class);
		toString = TagListSingleton.class.getDeclaredMethod("toString");

		// Initialize the fields
		tagToImg = TagListSingleton.class.getDeclaredField("tagToImg");
		tagToImg.setAccessible(true);
		HashMap<String, HashSet<String>> tags2image = new HashMap<String, HashSet<String>>();

		tags2image.put("tag1", new HashSet<String>());
		tags2image.get("tag1").add("img1");
		tags2image.get("tag1").add("img2");

		tags2image.put("tag2", new HashSet<String>());
		tags2image.get("tag2").add("img1");

		tagToImg.set(taglist, tags2image);

		imgToTag = TagListSingleton.class.getDeclaredField("imgToTag");
		imgToTag.setAccessible(true);
		HashMap<String, HashSet<String>> image2tags = new HashMap<String, HashSet<String>>();

		image2tags.put("img1", new HashSet<String>());
		image2tags.get("img1").add("tag1");
		image2tags.get("img1").add("tag2");

		image2tags.put("img2", new HashSet<String>());
		image2tags.get("img2").add("tag1");

		image2tags.put("img3", new HashSet<String>());

		imgToTag.set(taglist, image2tags);

	}

	/**
	 * Tests if all instances of TagListSingleton are the same.
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * 
	 */
	@Test
	public void testGetInstance() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object result1 = getInstance.invoke(taglist);

		taglist.addTag("path", "tag");
		Object result2 = getInstance.invoke(taglist);
		assertEquals(result1, result2);
	}

	/**
	 * Tests getting available tags if there are none. Should return empty set.
	 * 
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * 
	 */
	@Test
	public void testGetTagsNoTags() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

		HashMap<String, HashSet<String>> blank = new HashMap<String, HashSet<String>>();
		tagToImg.set(taglist, blank);
		imgToTag.set(taglist, blank);

		Object result = getTagsAll.invoke(taglist);

		assertEquals(new HashSet<String>(), result);
	}

	/**
	 * Tests getting available tags.
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetTags_withTags()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object result = getTagsAll.invoke(taglist);

		assertEquals(((Map<String, HashSet<String>>) tagToImg.get(taglist)).keySet(), result);
	}

	/**
	 * Tests getting tags for a specific image when is not known to tag manager.
	 * Returns empty set.
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * 
	 */
	@Test
	public void testGetTagsStringImageDoesNotExist()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		Object result = getTagsImage.invoke(taglist, "img10"); // img10 not in
																// data
																// structures

		assertEquals(new HashSet<String>(), result);

	}

	/**
	 * Tests getting tags for a specific image with no tags. Should return empty
	 * set.
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * 
	 * 
	 */
	@Test
	public void testGetTagsStringImageHasNoTags()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		Object result = getTagsImage.invoke(taglist, "img3"); // img3 has no
																// tags

		assertEquals(new HashSet<String>(), result);
	}

	/**
	 * Tests getting tags for a specific image.
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetTagsStringImageHasTags()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		Object result = getTagsImage.invoke(taglist, "img1");

		assertEquals(((Map<String, HashSet<String>>) imgToTag.get(taglist)).get("img1"), result);
	}

	/**
	 * Tests changing the pathname if original was not there to start with.
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testChangePathNamesDoesNotExist()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Map<String, HashSet<String>> before1 = ((Map<String, HashSet<String>>) imgToTag.get(taglist));
		Map<String, HashSet<String>> before2 = ((Map<String, HashSet<String>>) tagToImg.get(taglist));

		changePathNames.invoke(taglist, "img10", "img2");

		Map<String, HashSet<String>> after1 = ((Map<String, HashSet<String>>) imgToTag.get(taglist));
		Map<String, HashSet<String>> after2 = ((Map<String, HashSet<String>>) tagToImg.get(taglist));

		// Check that the data structures have not been mutated
		assertEquals(before1, after1);
		assertEquals(before2, after2);

	}

	/**
	 * Tests changing the pathname if the newer path name is already taken.
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testChangePathNamesNewAlreadyExists()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Map<String, HashSet<String>> before1 = ((Map<String, HashSet<String>>) imgToTag.get(taglist));
		Map<String, HashSet<String>> before2 = ((Map<String, HashSet<String>>) tagToImg.get(taglist));

		changePathNames.invoke(taglist, "img1", "img2");

		Map<String, HashSet<String>> after1 = ((Map<String, HashSet<String>>) imgToTag.get(taglist));
		Map<String, HashSet<String>> after2 = ((Map<String, HashSet<String>>) tagToImg.get(taglist));

		// Check that the data structures have not been mutated
		assertEquals(before1, after1);
		assertEquals(before2, after2);

	}

	/**
	 * Tests changing the pathname if original and new one are the same.
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testChangePathNamesPathNamesEqual()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Map<String, HashSet<String>> before1 = ((Map<String, HashSet<String>>) imgToTag.get(taglist));
		Map<String, HashSet<String>> before2 = ((Map<String, HashSet<String>>) tagToImg.get(taglist));

		changePathNames.invoke(taglist, "img2", "img2");

		Map<String, HashSet<String>> after1 = ((Map<String, HashSet<String>>) imgToTag.get(taglist));
		Map<String, HashSet<String>> after2 = ((Map<String, HashSet<String>>) tagToImg.get(taglist));

		// Check that the data structures have not been mutated
		assertEquals(before1, after1);
		assertEquals(before2, after2);

	}

	/**
	 * Tests changing the pathname.
	 * 
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * 
	 */

	@SuppressWarnings("unchecked")
	@Test
	public void testChangePathNamesExists()
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Map<String, HashSet<String>> before1 = new HashMap<String, HashSet<String>>();
		before1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> before2 = new HashMap<String, HashSet<String>>();
		before2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		changePathNames.invoke(taglist, "img2", "img10");

		Map<String, HashSet<String>> after1 = new HashMap<String, HashSet<String>>();
		after1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> after2 = new HashMap<String, HashSet<String>>();
		after2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		// Check that the data structures have not been mutated
		assertNotSame(before1, after1);
		assertEquals(before1.get("img2"), after1.get("img10"));
		assertTrue(after1.containsKey("img10"));
		assertFalse(after1.containsKey("img2"));

	}

	/**
	 * Tests removing a tag (that is known) from an image that is not known to
	 * the tag manager.
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testRemoveTagDoesntContainImage()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		Map<String, HashSet<String>> before1 = new HashMap<String, HashSet<String>>();
		before1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> before2 = new HashMap<String, HashSet<String>>();
		before2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		removeTag.invoke(taglist, "img10", "tag1");

		Map<String, HashSet<String>> after1 = new HashMap<String, HashSet<String>>();
		after1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> after2 = new HashMap<String, HashSet<String>>();
		after2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		assertEquals(before1, after1);
		assertEquals(before2, after2);
	}

	/**
	 * Tests removing a tag that is not known to the tag manager from an image
	 * (that is known).
	 * 
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testRemoveTagDoesntContainTag()
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Map<String, HashSet<String>> before1 = new HashMap<String, HashSet<String>>();
		before1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> before2 = new HashMap<String, HashSet<String>>();
		before2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		removeTag.invoke(taglist, "img1", "tag10");

		Map<String, HashSet<String>> after1 = new HashMap<String, HashSet<String>>();
		after1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> after2 = new HashMap<String, HashSet<String>>();
		after2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		assertEquals(before1, after1);
		assertEquals(before2, after2);
	}

	/**
	 * Tests removing a tag from an image that does not have that tag.
	 * 
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testRemoveTagImageDoesNotHaveTag()
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Map<String, HashSet<String>> before1 = new HashMap<String, HashSet<String>>();
		before1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> before2 = new HashMap<String, HashSet<String>>();
		before2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		removeTag.invoke(taglist, "img2", "tag2");

		Map<String, HashSet<String>> after1 = new HashMap<String, HashSet<String>>();
		after1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> after2 = new HashMap<String, HashSet<String>>();
		after2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		assertEquals(before1, after1);
		assertEquals(before2, after2);
	}

	/**
	 * Tests removing a tag that is the last tag of an image. Should also be
	 * removed from the available tags.
	 * 
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testRemoveTagLastTagtoRemove()
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Map<String, HashSet<String>> before1 = new HashMap<String, HashSet<String>>();
		before1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> before2 = new HashMap<String, HashSet<String>>();
		before2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		removeTag.invoke(taglist, "img1", "tag2"); // tag2 is only present in
													// img1

		Map<String, HashSet<String>> after1 = new HashMap<String, HashSet<String>>();
		after1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> after2 = new HashMap<String, HashSet<String>>();
		after2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		assertNotSame(before1, after1); // Check that the data structures have
										// been mutated
		assertFalse(after1.get("img1").contains("tag2")); // Check if that image
															// no longer has
															// that tag
		assertTrue(!after2.containsKey("tag2")); // Check if tag is still a part
													// of available tags

	}

	/**
	 * Tests removing a tag from an image where the tag still belongs to another
	 * image.
	 * 
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testRemoveTagNormal()
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Map<String, HashSet<String>> before1 = new HashMap<String, HashSet<String>>();
		before1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> before2 = new HashMap<String, HashSet<String>>();
		before2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		removeTag.invoke(taglist, "img2", "tag1"); // two images have tag1

		Map<String, HashSet<String>> after1 = new HashMap<String, HashSet<String>>();
		after1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> after2 = new HashMap<String, HashSet<String>>();
		after2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		assertNotSame(before1, after1); // Check that the data structures have
										// been mutated
		assertFalse(after1.get("img2").contains("tag1")); // Check if that image
															// no longer has
															// that tag
		assertTrue(after2.containsKey("tag1")); // Check if tag is still a part
												// of available tags
	}

	/**
	 * Tests removing all the tags from a single image when the image is not
	 * known to the tag manager.
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testRemoveAllImgTagImageDoesntExist()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Map<String, HashSet<String>> before1 = new HashMap<String, HashSet<String>>();
		before1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> before2 = new HashMap<String, HashSet<String>>();
		before2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		removeAllImgTag.invoke(taglist, "img10"); // No img10

		Map<String, HashSet<String>> after1 = new HashMap<String, HashSet<String>>();
		after1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> after2 = new HashMap<String, HashSet<String>>();
		after2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		// Check that nothing has been mutated
		assertEquals(before1, after1);
		assertEquals(before2, after2);
	}

	/**
	 * Tests removing all the tags from an image where the image has no tags.
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testRemoveAllImgTagImageHasNoTags()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Map<String, HashSet<String>> before1 = new HashMap<String, HashSet<String>>();
		before1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> before2 = new HashMap<String, HashSet<String>>();
		before2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		removeAllImgTag.invoke(taglist, "img3"); // img3 has no tags

		Map<String, HashSet<String>> after1 = new HashMap<String, HashSet<String>>();
		after1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> after2 = new HashMap<String, HashSet<String>>();
		after2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		// Check that nothing has been mutated
		assertEquals(before1, after1);
		assertEquals(before2, after2);
	}

	/**
	 * Tests removing all images from an image that has tags.
	 * 
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testRemoveAllImgTagImageHasTags()
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Map<String, HashSet<String>> before1 = new HashMap<String, HashSet<String>>();
		before1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> before2 = new HashMap<String, HashSet<String>>();
		before2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		removeAllImgTag.invoke(taglist, "img1"); // img1 has 2 tags

		Map<String, HashSet<String>> after1 = new HashMap<String, HashSet<String>>();
		after1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> after2 = new HashMap<String, HashSet<String>>();
		after2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		assertNotSame(before1, after1); // Check that the data structures have
										// been mutated
		assertTrue(after1.containsKey("img1")); // check if img1 is still known
												// to tag manager
		assertTrue(after1.get("img1").isEmpty()); // check if image has no more
													// tags

		// Check if tagToImg map has been changed too
		assertFalse(after2.get("tag1").contains("img1")); // Check if that tag
															// no longer has
															// that image
		assertTrue(after2.containsKey("tag1")); // Check if tag1 is still a part
												// of available tags

		assertTrue(!after2.containsKey("tag2")); // Check if tag2 is still a
													// part of available tags
													// (should not)
	}

	/**
	 * Tests adding a tag to an image not known by the tag manager.
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testAddTagImageDoesNotExist()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Map<String, HashSet<String>> before1 = new HashMap<String, HashSet<String>>();
		before1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> before2 = new HashMap<String, HashSet<String>>();
		before2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		addTag.invoke(taglist, "img10", "tag1"); // no such img10

		Map<String, HashSet<String>> after1 = new HashMap<String, HashSet<String>>();
		after1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> after2 = new HashMap<String, HashSet<String>>();
		after2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		assertNotSame(before1, after1); // Check that the data structures have
										// been mutated
		assertTrue(!before1.containsKey("img10")); // Make sure img10 was new
		assertTrue(after1.get("img10").contains("tag1")); // Check if tag has
															// been added to
															// image
		assertTrue(after2.get("tag1").contains("img10")); // Check if image has
															// been added to tag
	}

	/**
	 * Tests adding an invalid tag.
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testAddTagInvalidTag()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Map<String, HashSet<String>> before1 = new HashMap<String, HashSet<String>>();
		before1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> before2 = new HashMap<String, HashSet<String>>();
		before2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		addTag.invoke(taglist, "img1", "tag@1"); // tags should not contain @

		Map<String, HashSet<String>> after1 = new HashMap<String, HashSet<String>>();
		after1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> after2 = new HashMap<String, HashSet<String>>();
		after2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		// Check that nothing has been mutated
		assertEquals(before1, after1);
		assertEquals(before2, after2);
	}

	/**
	 * Tests adding a tag that the image already has.
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testAddTagImageAlreadyContainsTag()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Map<String, HashSet<String>> before1 = new HashMap<String, HashSet<String>>();
		before1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> before2 = new HashMap<String, HashSet<String>>();
		before2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		addTag.invoke(taglist, "img1", "tag1"); // img1 already has tag1 and
												// vice versa

		Map<String, HashSet<String>> after1 = new HashMap<String, HashSet<String>>();
		after1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> after2 = new HashMap<String, HashSet<String>>();
		after2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		// Check that nothing has been mutated
		assertEquals(before1, after1);
		assertEquals(before2, after2);
	}

	/**
	 * Tests adding a tag that an image does not have but is already present in
	 * available tags.
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testAddTagFromAvailableTags()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Map<String, HashSet<String>> before1 = new HashMap<String, HashSet<String>>();
		before1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> before2 = new HashMap<String, HashSet<String>>();
		before2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		addTag.invoke(taglist, "img2", "tag2"); // tag2 exists but does not
												// belong to img2

		Map<String, HashSet<String>> after1 = new HashMap<String, HashSet<String>>();
		after1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> after2 = new HashMap<String, HashSet<String>>();
		after2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		assertNotSame(before1, after1); // Check that the data structures have
										// been mutated
		assertTrue(after1.get("img2").contains("tag2")); // Check if tag has
															// been added to
															// image
		assertTrue(after2.get("tag2").contains("img2")); // Check if image has
															// been added to tag
	}

	/**
	 * Tests adding a new tag to an image that is not already in available tags.
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testAddTagNewTag() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Map<String, HashSet<String>> before1 = new HashMap<String, HashSet<String>>();
		before1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> before2 = new HashMap<String, HashSet<String>>();
		before2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		addTag.invoke(taglist, "img3", "tag4"); // tag4 is valid and is not part
												// of available tags

		Map<String, HashSet<String>> after1 = new HashMap<String, HashSet<String>>();
		after1.putAll(((Map<String, HashSet<String>>) imgToTag.get(taglist)));
		Map<String, HashSet<String>> after2 = new HashMap<String, HashSet<String>>();
		after2.putAll(((Map<String, HashSet<String>>) tagToImg.get(taglist)));

		assertNotSame(before1, after1); // Check that the data structures have
										// been mutated
		assertTrue(after1.get("img3").contains("tag4")); // Check if tag has
															// been added to
															// image
		assertTrue(after2.get("tag4").contains("img3")); // Check if image has
															// been added to tag
	}

	/**
	 * Tests the string representation of TagListSingleton when there are no tags.
	 * 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws InvocationTargetException 
	 * 
	 * */
	@Test
	public void testToStringNoTags() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		HashMap<String, HashSet<String>> blank = new HashMap<String, HashSet<String>>();
		tagToImg.set(taglist, blank);
		imgToTag.set(taglist, blank);
		String result = (String) toString.invoke(taglist);
		
		String expect = "";
		
		assertEquals(expect, result);
	}

	/**
	 * Tests the string representation of TagListSingleton when there are tags.
	 * 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * 
	 * */
	@Test
	public void testToStringWithTags() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String result = (String) toString.invoke(taglist);
		String expect1 = "tag1\ntag2\n";
		String expect2 = "tag2\ntag1\n";
		
		HashSet<String> expected =  new HashSet<String>();
		expected.add(expect1);
		expected.add(expect2);
		
		assertTrue(expected.contains(result));
	}

}
