package net.sf.bloodball;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import net.sf.bloodball.view.MainFrame;

public class SaveGameController {
	private static class SaveGameFileFilter extends FileFilter {
		public boolean accept(File file) {
			return file.isDirectory() || file.getName().endsWith(GAME_FILE_SUFFIX);
		}
		public String getDescription() {
			return GAME_FILE_DESCRIPTION;
		}
	}
	private final static String GAME_FILE_SUFFIX = ".blb";
	private final static String GAME_FILE_DESCRIPTION = "Blood Ball save game (*" + GAME_FILE_SUFFIX + ")";
	private net.sf.bloodball.view.MainFrame mainFrame;

	public SaveGameController(MainFrame mainframe) {
		this.mainFrame = mainFrame;
	}

	private File assureBloodballExtension(File saveFile) {
		if (!saveFile.getName().endsWith(GAME_FILE_SUFFIX)) {
			saveFile = new File(saveFile.getPath() + GAME_FILE_SUFFIX);
		}
		return saveFile;
	}

	private JFileChooser getOpenFileChooser() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new SaveGameFileFilter());
		fileChooser.showOpenDialog(mainFrame);
		return fileChooser;
	}

	private JFileChooser getSaveFileChooser() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new SaveGameFileFilter());
		fileChooser.showSaveDialog(mainFrame);
		return fileChooser;
	}
}