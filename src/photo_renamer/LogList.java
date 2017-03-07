package photo_renamer;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * The loglist that manages all the logs in the program.
 * 
 * @author Diadem Shoukralla, Juliaan Posaratnanathan
 */
public class LogList {

	/**
	 * The path to the file with log information.
	 */
	private static String pathName;

	/**
	 * The set of all logs for the whole project.
	 */
	private static HashSet<Log> logList;

	/**
	 * A mapping of dates to the logs that occurred during it.
	 */
	private static Map<String, HashSet<Log>> logDateMap; // date to loglist

	/**
	 * A mapping of pathnames referring to images to logs that belong to it.
	 */
	private static Map<String, HashSet<Log>> logImgMap; // pathname to log list

	/**
	 * Initializes the LogList's data structures by reading from the log file.
	 */
	public static void makeLogList() {
		pathName = "log.txt";
		try {
			File f = new File(pathName);
			if (f.exists()) {
				readFromFile();
			} else {
				f.createNewFile();
				logList = new HashSet<Log>();
			}
		} catch (NotSerializableException e) {
			System.out.println(logList);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("why");
		}
		makeDateMap();
		makeImgMap();
	}

	/**
	 * Reads the serialized set of logs from the log file.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * 
	 * @see ClassNotFoundException, IOException
	 */
	@SuppressWarnings("unchecked")
	public static void readFromFile() throws ClassNotFoundException, IOException {

		try {
			InputStream file = new FileInputStream(pathName);
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);


			logList = (HashSet<Log>) input.readObject();

			input.close();
			file.close();
		} catch (EOFException e) {
			logList = new HashSet<Log>();
		} catch (InvalidClassException e) {
			System.out.println("file doesnt match the type");
		}
	}

	/**
	 * Gets the set of Logs for the whole project.
	 * 
	 * @return The set of Logs for the whole project.
	 */
	public static HashSet<Log> getLogList() {
		return logList;
	}

	/**
	 * Makes the map of the pathname's referring to images to the logs
	 * pertaining to that image.
	 * 
	 * @see Log
	 */
	private static void makeImgMap() {
		logImgMap = new HashMap<String, HashSet<Log>>();
		for (Log log : logList) {
			String key = log.getImg();
			if (logImgMap.containsKey(key))
				logImgMap.get(key).add(log);
			else {
				HashSet<Log> imgLog = new HashSet<Log>();
				imgLog.add(log);
				logImgMap.put(key, imgLog);
			}
		}

	}

	/**
	 * Gets the set of logs pertaining to a specific image.
	 * 
	 * @param image
	 *            The image to find the logs that belong to it.
	 * @return The set of logs pertaining to the image.
	 * 
	 * @see ImageNode
	 */
	public static HashSet<Log> getLogImg(ImageNode image) {
		if (logImgMap.get(image.getPathName()) != null)
			return logImgMap.get(image.getPathName());
		return new HashSet<Log>();
	}

	/**
	 * Gets a set of past names an image has ever had.
	 * 
	 * @param img
	 *            The image to find the past names of.
	 * @return The set of past names of the image.
	 * 
	 * @see ImageNode, Log
	 */
	public static HashSet<String> getPastNames(ImageNode img) {
		HashSet<String> pastNames = new HashSet<String>();
		if (logImgMap.get(img.getPathName()) != null) {
			for (Log log : logImgMap.get(img.getPathName()))
				pastNames.add(log.getOldName());
		}
		return pastNames;
	}

	/**
	 * Makes the map of a string version of dates to the logs that occurred that
	 * date.
	 * 
	 * @see Log
	 */
	private static void makeDateMap() {
		logDateMap = new HashMap<String, HashSet<Log>>();
		for (Log log : logList) {
			String key = log.getTimeStamp().toString().substring(0, 10);
			if (logDateMap.containsKey(key))
				logDateMap.get(key).add(log);
			else {
				HashSet<Log> dateLog = new HashSet<Log>();
				dateLog.add(log);
				logDateMap.put(key, dateLog);
			}
		}
	}

	/**
	 * Writes the LogList to a file.
	 */
	public static void writeToFile() {
		try {
			FileOutputStream file = new FileOutputStream(pathName);
			ObjectOutputStream output = new ObjectOutputStream(file);
			output.writeObject(logList);
			output.close();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("why");
		}
	}

	/**
	 * Adds a log to all log list data structures in this class.
	 * 
	 * @param log
	 *            The log to be added.
	 * 
	 * @see Log, ImageNode
	 */
	public static void addLog(Log log) {
		logList.add(log);
		String date = log.getTimeStamp().toString().substring(0, 10);
		if (logDateMap.containsKey(date))
			logDateMap.get(date).add(log);
		else {
			logDateMap.put(date, new HashSet<Log>());
			logDateMap.get(date).add(log);
		}

		String img = log.getImg();
		if (logImgMap.containsKey(img))
			logImgMap.get(img).add(log);
		else {
			logImgMap.put(img, new HashSet<Log>());
			logImgMap.get(img).add(log);
		}
	}

	/**
	 * 
	 * Updates the log data structures for a pathname change, as given by the
	 * parameters.
	 * 
	 * @param oldPathName
	 *            The original pathname that's changing.
	 * @param newPathName
	 *            The new pathname thats being implemented.
	 * 
	 * @see Log
	 */
	public static void changePathName(String oldPathName, String newPathName) {
		for (Log log : logImgMap.get(oldPathName))
			log.setPathName(newPathName);
		logImgMap.put(newPathName, logImgMap.get(oldPathName));
		logImgMap.remove(oldPathName);
	}

	/**
	 * Gets a mapping of dates to the logs that occurred during it.
	 * 
	 * @return The map of dates to logs that occurred during the date.
	 */
	public static Map<String, HashSet<Log>> getLogDateMap() {
		return logDateMap;

	}

	/**
	 * Gets one log that refers to the given image that has the given past name.
	 * 
	 * @param img
	 *            The image that the log must pertain to.
	 * @param pastName
	 *            A previous name of the image.
	 * @return A log that pertains to the image and has the given past name.
	 */
	public static Log getLog(ImageNode img, String pastName) {

		HashSet<Log> imgLogs = getLogImg(img);
		for (Log log : imgLogs) {
			if (log.getOldName().equals(pastName))
				return log;
			
		}
		return null;
	}

	/**
	 * Returns the Logs formatted as a string.
	 * 
	 * @return The string representation of the logs.
	 */
	public static String viewLogs() {
		String logString = "";
		List<Log> sortedLogList = new ArrayList<Log>(logList);
		Collections.sort(sortedLogList);
		for (Log log : sortedLogList)
			logString += log + "\n";
		return logString;
	}

}
