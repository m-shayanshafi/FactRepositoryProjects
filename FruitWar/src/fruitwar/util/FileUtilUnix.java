package fruitwar.util;

import java.io.File;

class FileUtilUnix extends FileUtil {

	protected boolean copyFileImpl(String src, String dst) {
		boolean success = false;
		try {
			success = 0 == SysCmd.exec("cp " + src + " " + dst);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

	protected boolean moveFileImpl(String src, String dst) {
		boolean success = false;
		try {
			success = 0 == SysCmd.exec("mv " + src + " " + dst);
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
			//clean up dir content
			success = 0 == SysCmd.exec("rm -rf " + dir + "/*");
		} catch (Exception e) {
		}
		return success;
	}
}
