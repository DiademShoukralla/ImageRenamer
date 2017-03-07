package photo_renamer;

import java.util.Map;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.awt.Image;

/**
 * The root of a tree representing a directory structure.
 * 
 * @author Diadem Shoukralla, Juliaan Posaratnanathan
 */
public class FileNode {

	/**
	 * The top most root of the tree.
	 */
	private static FileNode root;

	/**
	 * Sets the top of the tree.
	 * 
	 * @param root
	 *            The topmost root of the tree.
	 */
	public static void setRoot(FileNode root) {
		FileNode.root = root;
	}

	/**
	 * Gets the topmost root of the tree.
	 * 
	 * @return The top most root of the tree.
	 */
	public static FileNode getRoot() {
		return root;
	}

	/** The name of the file or directory this node represents. */
	private String name;
	/** Whether this node represents a file or a directory. */
	private FileType type;
	/** This node's parent. */
	private FileNode parent;

	/**
	 * The absolute pathname of the file this node is referring to.
	 */
	private String pathName;

	/**
	 * This node's children, mapped from the file names to the nodes. If type is
	 * FileType.FILE, this is null.
	 * 
	 */
	private Map<String, FileNode> children;

	/**
	 * Initializes a generic FileNode.
	 */
	public FileNode() {
		super();
	}

	/**
	 * A node in this tree.
	 *
	 * @param name
	 *            the file
	 * @param parent
	 *            the parent node.
	 * @param type
	 *            file or directory
	 * @see buildFileTree
	 */
	public FileNode(String name, FileNode parent, FileType type, String pathName) {
		this.name = name; // Node's name
		this.parent = parent; // Node's parent
		this.type = type; // Nodes type
		this.children = null;
		if (type == FileType.DIRECTORY)
			this.children = new HashMap<String, FileNode>(); // Node's Children
		this.pathName = pathName;

	}

	/**
	 * Return the name of the file or directory represented by this node.
	 *
	 * @return name of this Node
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set the name of the current node
	 *
	 * @param name
	 *            of the file/directory
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return the child nodes of this node.
	 *
	 * @return the child nodes directly underneath this node.
	 */
	public Collection<FileNode> getChildren() {
		return this.children.values();
	}

	/**
	 * Return this node's parent.
	 * 
	 * @return the parent
	 */
	public FileNode getParent() {
		return parent;
	}

	/**
	 * Set this node's parent to p.
	 * 
	 * @param p
	 *            the parent to set
	 */
	public void setParent(FileNode p) {
		this.parent = p;
	}

	/**
	 * Add childNode, representing a file or directory named name, as a child of
	 * this node.
	 * 
	 * @param name
	 *            the name of the file or directory
	 * @param childNode
	 *            the node to add as a child
	 */
	public void addChild(String name, FileNode childNode) {
		this.children.put(name, childNode);
	}

	/**
	 * Return whether this node represents a directory.
	 * 
	 * @return whether this node represents a directory.
	 */
	public boolean isDirectory() {
		return this.type == FileType.DIRECTORY;
	}

	/**
	 * Build the tree of nodes rooted at file in the file system; note curr is
	 * the FileNode corresponding to file, so this only adds nodes for children
	 * of file to the tree. Precondition: file represents a directory.
	 * 
	 * @param file
	 *            the file or directory we are building
	 * @param curr
	 *            the node representing file
	 */
	public static void buildTree(File file, FileNode curr) {
		// File always going to be a directory
		File[] children = file.listFiles(); // Files and directories in file

		for (File child : children) {
			// Check if directory here

			// If the child is not a directory
			if (!child.isDirectory()) {
				// Create the node then add as a child to curr
				// first check if its an image file
				try {
					Image image = ImageIO.read(child);
					// Check if it's an actual image
					if (image != null) {
						ImageNode babyNode = new ImageNode(child.getName(), curr, FileType.FILE,
								child.getAbsolutePath());
						curr.addChild(child.getName(), babyNode);
					}
				} catch (IOException e) {
					System.out.println(file + " ------- " + child);
				} catch (NullPointerException e) {
					System.out.println("This is not a file!!");
				}
				continue;
			}
			// If child is a directory
			// Create the node then add as a child to curr
			// Hidden files scare us
			if (!child.isHidden()) {
				FileNode babyNode = new FileNode(child.getName(), curr, FileType.DIRECTORY, child.getAbsolutePath());
				curr.addChild(child.getName(), babyNode);

				buildTree(child, babyNode); // Recursively build tree
			}
		}
	}

	/**
	 * Gets the absolute pathname of the file this node refers to.
	 * 
	 * @return The absolute pathname of the file this node is referring to.
	 */
	public String getPathName() {
		return pathName;
	}

	/**
	 * Sets the absolute pathname of the file this node refers to.
	 * 
	 * @param pathName
	 *            The absolute pathname of the file this node is referring to.
	 */
	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

}
