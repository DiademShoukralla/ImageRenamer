package photo_renamer;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 
 * A log representing a change in name for some image.
 * 
 * @author Diadem Shoukralla, Juliaan Posaratnanathan
 *
 */
public class Log implements Serializable, Comparable<Log> {

	/**
	 * Log's serial number.
	 */
	private static final long serialVersionUID = 45897L;
	/**
	 * The timestamp when this Log was created.
	 */
	private Timestamp timeStamp;
	/**
	 * The new name of the image this Log refers to.
	 */
	private String newName;
	/**
	 * The old name of the image this Log refers to.
	 */
	private String oldName;
	/**
	 * The pathname of the image this Log refers to.
	 */
	private String pathName;

	/**
	 * A log keeping track of an image's name change.
	 * 
	 * @param time
	 *            The timestamp when the name change occurred.
	 * @param newS
	 *            The new name of the image the Log refers to.
	 * @param oldS
	 *            The old name of the image the Log refers to.
	 * @param img
	 *            The image the Log refers to.
	 * 
	 * @see Timestamp
	 */
	public Log(Timestamp time, String newS, String oldS, String img) {
		timeStamp = time;
		newName = newS;
		oldName = oldS;
		this.pathName = img;
	}

	/**
	 * Initializes a generic log.
	 */
	public Log() {
	}

	/**
	 * Gets the pathname of the image this Log refers to.
	 * 
	 * @return The pathname of the image the log refers to.
	 */
	public String getImg() {
		return pathName;
	}

	/**
	 * Sets the pathname of the image this Log refers to.
	 * 
	 * @param pathName
	 *            The pathname of the image this log refers to.
	 */
	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	/**
	 * Gets the time-stamp of the image this Log refers to.
	 * 
	 * @return The time-stamp when this Log was created.
	 */
	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Sets the time-stamp of the image this Log refers to.
	 * 
	 * @param timeStamp
	 *            The time-stamp when this Log was created.
	 */
	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * Gets the newname of the image this Log refers to.
	 * 
	 * @return The new name of the image the Log refers to.
	 */
	public String getNewName() {
		return newName;
	}

	/**
	 * Sets the newname of the image this Log refers to.
	 * 
	 * @param newName
	 *            The new name of the image the Log refers to.
	 */
	public void setNewName(String newName) {
		this.newName = newName;
	}

	/**
	 * Gets the oldname of the image this Log refers to.
	 * 
	 * @return The old name of the image the Log refers to.
	 */
	public String getOldName() {
		return oldName;
	}

	/**
	 * Sets the oldname of the image this Log refers to.
	 * 
	 * @param oldName
	 *            The old name of the image the Log refers to.
	 */
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return timeStamp + " :: \n      " + oldName + " -> " + newName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Log other) {
		// This < other negative
		// older timestamp < newer timestamp
		if (timeStamp.getTime() < other.getTimeStamp().getTime())
			return -1;
		if (timeStamp.getTime() > other.getTimeStamp().getTime())
			return 1;
		return 0;
	}

}
