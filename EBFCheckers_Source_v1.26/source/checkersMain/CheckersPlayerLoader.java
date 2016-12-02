package checkersMain;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JOptionPane;

import checkersPlayer.Human;

/**
 * A final class that is not instantiable. It provides static methods to load
 * the {@link CheckersPlayerInterface}s in the {@link checkersPlayer} package
 * and retrieve their classes and display names.
 * 
 * @author Amos Yuen
 * @version 1.00 - 25 July 2008
 */
public final class CheckersPlayerLoader {

	/**
	 * A file filter for java's ".class" files.
	 * 
	 * @author Amos Yuen
	 * @version 1.00 - 6 July 2008
	 */
	private static class JavaClassFileFilter implements FileFilter {
		public boolean accept(File pathname) {
			System.out.println("Found " + pathname.getName());
			return pathname.isDirectory() || accept(pathname.getName());
		}

		public boolean accept(String pathname) {
			return pathname.endsWith(".class");
		}
	}

	/**
	 * A list of the classes of the loaded {@link CheckersPlayerInterface}s.
	 */
	private static ArrayList<Class<? extends CheckersPlayerInterface>> classes;

	/**
	 * A list of the display names of the loaded {@link CheckersPlayerInterface}
	 * s.
	 */
	private static ArrayList<String> displayNames;

	/**
	 * A string representing the base directory to search for
	 * {@link CheckersPlayerInterface}s.
	 */
	public static final String FOLDER_DIRECTORY = "checkersPlayer";

	private static JavaClassFileFilter javaFileFilter = new JavaClassFileFilter();

	static {
		classes = new ArrayList<Class<? extends CheckersPlayerInterface>>();
		displayNames = new ArrayList<String>();

		classes.add(Human.class);
		displayNames.add(new Human().getName());

		loadCheckersPlayers();
	}

	/**
	 * Attempts to create an instance of the class specified by the classPath
	 * and cast it to a {@link CheckersPlayerInterface}. If successful, it will
	 * add the class to {@link #classes} and the display name of the
	 * {@link CheckersPlayerInterface} to {@link #displayNames}.
	 * 
	 * @param classPath
	 *            - the classPath of the class to be added
	 * @return whether the class was added successfully
	 */
	@SuppressWarnings("unchecked")
	private static boolean addPlayer(String classPath) {
		try {
			classPath = classPath.replace('/', '.');
			classPath = classPath.substring(0, classPath.indexOf(".class"));
			// System.out.println("path: " + classPath);
			Class c = Class.forName(classPath);

			if (CheckersPlayerInterface.class.isAssignableFrom(c)) {
				Class<? extends CheckersPlayerInterface> cpClass = c
						.asSubclass(CheckersPlayerInterface.class);
				if (classes.contains(cpClass))
					return false;

				classes.add(c);
				displayNames.add(cpClass.newInstance().getName());
				System.out.println("Loaded: " + classPath);
				return true;
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Returns the number of {@link CheckersPlayerInterface}s that have been
	 * loaded.
	 * 
	 * @return the number of {@link CheckersPlayerInterface}s that have been
	 *         loaded
	 */
	public static int getNumCheckersPlayers() {
		return classes.size();
	}

	/**
	 * Returns the {@link CheckersPlayerInterface}'s class at the passed index.
	 * 
	 * @param index
	 *            - the index of the {@link CheckersPlayerInterface}'s class to
	 *            get
	 * @return the {@link CheckersPlayerInterface}'s class at the passed index
	 */
	public static Class<? extends CheckersPlayerInterface> getPlayerClass(
			int index) {
		return classes.get(index);
	}

	/**
	 * Returns an array of the {@link CheckersPlayerInterface}s' classes that
	 * have been loaded.
	 * 
	 * @return an array of the {@link CheckersPlayerInterface}s classes that
	 *         have been loaded
	 */
	@SuppressWarnings("unchecked")
	public static Class<? extends CheckersPlayerInterface>[] getPlayerClasses() {
		return classes.toArray(new Class[classes.size()]);
	}

	/**
	 * Returns the {@link CheckersPlayerInterface}'s display name at the passed
	 * index.
	 * 
	 * @param index
	 *            - the index of the {@link CheckersPlayerInterface}'s display
	 *            name to get
	 * @return the {@link CheckersPlayerInterface}'s display name at the passed
	 *         index
	 */
	public static String getPlayerDisplayName(int index) {
		return displayNames.get(index);
	}

	/**
	 * Returns an array of the {@link CheckersPlayerInterface}s' display names
	 * that have been loaded.
	 * 
	 * @return an array of the {@link CheckersPlayerInterface}s display names
	 *         that have been loaded
	 */
	public static String[] getPlayerDisplayNames() {
		return displayNames.toArray(new String[displayNames.size()]);
	}

	/**
	 * Searches the {@link checkersPlayer} package for
	 * {@link CheckersPlayerInterface}s and attempts to add them using
	 * {@link #addPlayer(String)}.
	 */
	public static void loadCheckersPlayers() {
		URL url = CheckersPlayerLoader.class.getProtectionDomain()
				.getCodeSource().getLocation();
		try {
			System.out.println("\nCheckers Player Loader");
			System.out.println("URI of Source: " + url.toURI());
			loadPlayersHelper(new File(url.toURI().getPath() + FOLDER_DIRECTORY));
		} catch (Exception e) {
			try {
				JarFile jar = new JarFile(url.toURI().getPath());
				Enumeration<JarEntry> entries = jar.entries();
				while (entries.hasMoreElements()) {
					String name = entries.nextElement().getName();
					if (name.startsWith(FOLDER_DIRECTORY)
							&& javaFileFilter.accept(name)) {
						addPlayer(name);
					}
				}
			} catch (Exception e2) {
				JOptionPane.showMessageDialog(null, e2.getMessage(),
						"Error Loading Players", JOptionPane.ERROR_MESSAGE);
			}
		}

		quickSort();
		System.out.println("Finished Searching\n");
	}

	/**
	 * A recursive helper that searches all files in the sub directories of the
	 * passed directory.
	 */
	private static void loadPlayersHelper(File directory) {
		System.out.println("Searching " + directory.toURI().getPath());
		File[] files = directory.listFiles(javaFileFilter);
		for (File file : files) {
			if (file.isDirectory())
				loadPlayersHelper(file);
			else {
				String path = file.toURI().getPath();
				addPlayer(path.substring(path.indexOf(FOLDER_DIRECTORY)));
			}
		}
	}

	/**
	 * Sorts the {@link CheckersPlayerInterface}s alphabetically.
	 */
	private static void quickSort() {
		quickSort(0, classes.size() - 1);
	}

	/**
	 * Sorts the {@link CheckersPlayerInterface}s alphabetically.
	 */
	private static void quickSort(int left, int right) {
		if (left >= right)
			return;

		int i = left - 1;
		int j = right;
		int pivotIndex = (i + j) / 2;
		swap(pivotIndex, right);
		String pivotValue = displayNames.get(right).toLowerCase();
		while (true) {
			while (pivotValue.compareTo(displayNames.get(++i).toLowerCase()) > 0) {
				if (i == right)
					break;
			}
			while (pivotValue.compareTo(displayNames.get(--j).toLowerCase()) < 0) {
				if (j == left)
					break;
			}
			if (i >= j)
				break;
			swap(i, j);
		}
		swap(i, right);

		quickSort(left, i - 1);
		quickSort(i + 1, right);
	}

	private static void swap(int i, int j) {
		Class<? extends CheckersPlayerInterface> c = classes.get(i);
		classes.set(i, classes.get(j));
		classes.set(j, c);
		String name = displayNames.get(i);
		displayNames.set(i, displayNames.get(j));
		displayNames.set(j, name);
	}

	private CheckersPlayerLoader() {
		super();
	}
}