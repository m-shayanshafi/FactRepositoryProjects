package fruitwar.util;

import java.io.File;

public class FileUtilOS400 extends FileUtil {

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
			
			String[] cmds = new String[3];
			cmds[0] = "sh";
			cmds[1] = "-c";
			cmds[2] = "rm -rf " + f.getCanonicalPath() + "/*";
			
			//clean up dir content
			success = 0 == SysCmd.exec(new SysCmd.CmdWrapper(cmds));
		} catch (Exception e) {
		}
		return success;
	}
}
