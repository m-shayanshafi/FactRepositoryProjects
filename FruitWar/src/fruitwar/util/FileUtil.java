package fruitwar.util;

import java.io.File;

/**
 * File related operations. Hide platform details.
 * 
 * @author wangnan
 *
 */
public abstract class FileUtil {

	private static FileUtil instance = null;
	
	static {
		String osName = System.getProperty("os.name");
        //String osVersion = System.getProperty("os.version");
        
        if(osName.startsWith("Windows"))
        	instance = new FileUtilWin32();
        else if(osName.equals("Linux") || osName.equals("AIX"))
        	instance = new FileUtilUnix();
        else if(osName.equals("OS/400"))
        	instance = new FileUtilOS400();
        else
        	throw new RuntimeException("Unsupported platform: " + osName);
	}
	
	
	
	public static boolean move(String src, String dst){
		return instance == null ? false : instance.moveFileImpl(src, dst);
	}
	
	public static boolean copy(String src, String dst){
		return instance == null ? false : instance.copyFileImpl(src, dst);
	}
	
	/**
	 * Clean everything under the given directory. Sub dirs are unchanged.
	 * @param path
	 * @return
	 */
	public static boolean cleanDirFiles(String path){
		return instance == null ? false : instance.cleanDirFilesImpl(path);
	}
	
	public static boolean makeDirs(String path){
		File f = new File(path);
		if(f.exists() && f.isDirectory())
			return true;
		return f.mkdirs();
	}
	
	protected abstract boolean moveFileImpl(String src, String dst);
	protected abstract boolean copyFileImpl(String src, String dst);
	protected abstract boolean cleanDirFilesImpl(String dir);

	
}
