package fruitwar.util;

import java.io.File;

class FileUtilWin32 extends FileUtil {

	protected boolean copyFileImpl(String src, String dst) {
		boolean success = false;
		try {
			src = formalizeFilePath(src);
			dst = formalizeFilePath(dst);
			success = 0 == SysCmd.exec("cmd.exe /C copy /Y " + src + " " + dst);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

	protected boolean moveFileImpl(String src, String dst) {
		boolean success = false;
		try {
			src = formalizeFilePath(src);
			dst = formalizeFilePath(dst);
			success = 0 == SysCmd.exec("cmd.exe /C move " + src + " " + dst);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

	protected boolean cleanDirFilesImpl(String dir) {
		File f = new File(dir);
		if(!f.exists() || !f.isDirectory())
			return false;
		
		boolean success = false;
		try {
			dir = f.getCanonicalPath();
			//success = 0 == SysCmd.exec("cmd.exe /C del /Q /S " + dir);
			//on windows, remove the dir, and recreate it.
			success = 0 == SysCmd.exec("cmd.exe /C rd /Q /S " + dir);
			success |= makeDirs(dir);
		} catch (Exception e) {
		}
		return success;
		
	}
	
	private static String formalizeFilePath(String s){
		//This can not handle file names contain "*"
		//s = new File(s).getCanonicalPath();
		return s.replace('/', '\\');
	}
}
