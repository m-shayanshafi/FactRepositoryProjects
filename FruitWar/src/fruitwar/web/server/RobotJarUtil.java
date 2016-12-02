package fruitwar.web.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

import fruitwar.supp.RobotProps;
import fruitwar.util.FileUtil;
import fruitwar.util.Logger;
import fruitwar.util.SysCmd;

/**
 * This class consists tools for handling FruitWar Robot jar file.
 * 
 * Actually a FruitWar Robot Jar file is a java jar file which
 * contains "fruitwar.properties" at the top level directory.
 * The key property in the properties file is the robot class.
 * See class RobotProperties for property definitions.
 * 
 * This class is not designed as static util package is for
 * flexible logging. An instance should be initialized with a 
 * PrintWriter. It writes messages to standard logger as well
 * as to the given PrintWriter. If there's no print writer provided,
 * this feature is disabled. Another concern is that, multiple thread
 * may use different instances of this class, and each one has there
 * own PrintWriter object. So standard java logger/handler method is
 * also not suitable for this.  
 * 
 * @author wangnan
 *
 */
public class RobotJarUtil {

	public static final String FRUITWAR_PROP_ENTRY_NAME = "fruitwar.properties";

	/**
	 * Contain robot class name info.
	 *  
	 * @author wangnan
	 *
	 */
	public static class ClassNameInfo{
		String packageName;	//package name
		String className;	//the class name without package name 
		public String getFullName(){
			if(packageName == null)
				return className;
			return packageName + '.' + className;
		}
		public String getShortName(){
			return className;
		}
		public String getPackageName(){
			return packageName;
		}
	}
	

	/**
	 * pattern to match the main class.
	 * This is a simple match, we do not handle complex case.
	 * sample:
	 * 		public class RandomRobot implements IFruitWarRobot {
	 */
	private static Pattern classNamePattern = Pattern.compile("public\\s*class\\s*(.*)\\s*implements\\s*IFruitWarRobot\\s*.*");
	/**
	 * pattern to match the package declaration
	 * This is a simple match, we do not handle complex case.
	 * sample:
	 * 		package aaa.bbb;
	 */
	private static Pattern packageNamePattern = Pattern.compile("package\\s*(.*)\\s*;");
	
	private PrintWriter out;
	private PrintWriter err;
	private boolean disableOut;
	
	public RobotJarUtil(PrintWriter out, PrintWriter err){
		this.out = out;
		this.err = err;
	}
	
	/**
	 * Package a jar file to a FruitWar Robot jar file, with
	 * the given properties.
	 * @param jarFileName
	 * @return
	 */
	public boolean convertJarToFWRJ(String jarFileName, RobotProps prop){
		return prop.updateToFile(jarFileName);
	}

	/**
	 * Compile & package a single java source file into a FruitWar 
	 * Robot Jar file, with the given properties.
	 * 
	 * This method depends on dir structure and must be run in
	 * FruitWar server environment.
	 * 
	 * @param fileName
	 */
	public boolean packFileToFWRJ(String javaFileName, RobotProps prop){

		if(!javaFileName.toLowerCase().endsWith(".java")){
			log("ERROR! -- The input file is not a java source file: " + javaFileName);
			return false;
		}
		
		//compile the java source and package it to a jar file
		String jarFileName = covertJavaSourceToJar(javaFileName);
		
		//if compilation failed...
		if(jarFileName == null)
			return false;
		
		//inject properties to jar, to make it
		//a valid FruitWar Robot JAR
		if(!convertJarToFWRJ(jarFileName, prop))
			return false;
		
		//move the robot to valid position
		boolean ret = publishRobotJar(jarFileName);
		
		return ret;
	}

	/**
	 * Identify robot class short name from the given source.
	 * @param src
	 * @throws IOException 
	 */
	public ClassNameInfo identifyRobotClassName(String src) {
		BufferedReader rdr = new BufferedReader(new StringReader(src));
		ClassNameInfo cni = null;
		
		try {
			cni = identifyRobotClassName(rdr);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				rdr.close();
			} catch (IOException e) {}
		}
		
		return cni;
	}
	
	/**
	 * Get source file from robot jar. This function only applies to
	 * simple robot jar, which is created from a single java source
	 * file. No checking for simple robot jar in this function. It
	 * directly retrieves the java file according to the robot main
	 * class.
	 * @param robotName
	 * @return
	 */
	public static StringBuffer getSimpleRobotSrc(String robotName){
		//make target jar file name
		String fileName = ServerConfig.getRobotPath() + robotName + ".jar";	//ServerConfig.ROBOT_FILE_EXT;
		
		//get the main class name. We use it to locate the java source in the jar.
		RobotProps prop = RobotProps.loadFromFile(fileName);
		if(prop == null)
			return null;
		String sourceEntryName = prop.getName().replaceAll("\\.", "/");
		sourceEntryName += ".java";
		
		//retrieve the source file
		StringBuffer buf = new StringBuffer();
		JarFile jar = null;
		InputStream is = null;
		InputStreamReader rdr = null;
		boolean success = false;
		try{
			
			jar = new JarFile(fileName);
			ZipEntry ze = jar.getEntry(sourceEntryName);
			is = jar.getInputStream(ze);
			rdr = new InputStreamReader(is);
			
			char[] cbuf = new char[8192];
			while(true){
				int n = rdr.read(cbuf);
				if(n == -1)
					break;
				buf.append(cbuf, 0, n);
			}
			success = true;
		}catch(IOException e){
			Logger.exception(e);
		}finally{
			if(rdr != null){
				try {
					rdr.close();
				} catch (IOException e) {
				}
			}
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
				}
			}
			if(jar != null){
				try {
					jar.close();
				} catch (IOException e) {
				}
			}
		}
		
		return success ? buf : null;
	}
	
	/**
	 * Identify robot class short name from the given source reader.
	 * @param src
	 * @throws IOException 
	 */
	public ClassNameInfo identifyRobotClassName(BufferedReader rdr) throws IOException{
		
		ClassNameInfo info = new ClassNameInfo();
		
		String s;
		while((s = rdr.readLine()) != null){
			s = s.trim();
			
			//is it package declaration?
			Matcher m = packageNamePattern.matcher(s);
			if(m.matches()){
				if(info.packageName != null){
					return null;
				}
				String name = m.group(1);
				if(name == null)
					return null;
				name = name.trim();
				if(name.length() == 0)
					return null;
				if(name.indexOf(" ") > 0 || name.indexOf("	") > 0 
						|| name.indexOf("\r") > 0 || name.indexOf("\n") > 0)
					return null;
				
				info.packageName = name;
				continue;
			}
			
			//is it class declaration?
			m = classNamePattern.matcher(s);
			if(m.matches()){
				String name = m.group(1);
				
				//validation of class name.
				//Is there a API to check valid java class name before 1.4?
				//1.6 has SourceVersion.isIdentifier...
				
				if(name == null)
					return null;
				name = name.trim();
				if(name.length() == 0)
					return null;
				if(name.indexOf(" ") > 0 || name.indexOf("	") > 0 
						|| name.indexOf("\r") > 0 || name.indexOf("\n") > 0)
					return null;
				
				info.className = name;
				
				//done
				return info;
			}
		}
		return null;
	}

	private boolean publishRobotJar(String jarFileName) {
		File f = new File(jarFileName);
		String destFileName = ServerConfig.getRobotPath() + f.getName();
		
		//delete file if exists
		new File(destFileName).delete();
		
		return FileUtil.move(jarFileName, destFileName);
	}

	/**
	 * Compile the given java file into a jar.
	 * This includes:
	 * 	1. compile the source file into the same directory,
	 *  2. if there's customized package, the same dir structure will
	 *     be created and the compiled files together with the source
	 *     will be moved there.
	 *  3. package all files(*.java *.class), or the package directory 
	 *     just created, into a jar.
	 *  
	 * The jar name is the full class name (with package name).
	 * 
	 * @param fileName
	 * @return null if any error occurs.
	 */
	private String covertJavaSourceToJar(String fileName) {
		//
		//	get class name with package name	
		//
		ClassNameInfo cni = null;
		BufferedReader rdr = null;
		try {
			rdr = new BufferedReader(new FileReader(fileName));
			cni = identifyRobotClassName(rdr);
		} catch (IOException e) {
			e.printStackTrace();
			log("Exception=" + e);
		}finally{
			if(rdr != null){
				try {
					rdr.close();
				} catch (IOException e) {}
			}
		}
		
		if(cni == null){
			log("Error identifying class name");
			return null;
		}
		
		//
		//compile
		//
		if(!system("javac -classpath " + getClasspath() + " " + fileName))
			return null;
		
		//
		//package
		//
		//prepare jar name
		String basePath = new File(fileName).getParent() + File.separatorChar;
		String jarName = basePath + cni.getFullName() + ".jar";
		
		String packageCommand;
		if(cni.getPackageName() != null){
			String dir = cni.getPackageName().replace('.', File.separatorChar);
			String destDir = basePath  + dir; 
			if(!FileUtil.makeDirs(destDir)){
				log("Error making dir: " + destDir);
				return null;
			}
			
			disableOut = true;
			if(!FileUtil.move(basePath + "*.class", destDir))
				return null;
			if(!FileUtil.move(basePath + "*.java", destDir))
				return null;
			disableOut = false;
			
			//package the directory
			packageCommand = "jar cf " + jarName + " -C " + basePath + " " + dir;
		}else{
			//this is irritating...
			File f = new File(basePath);
			String[] filenames = f.list(new FilenameFilter(){
				public boolean accept(File dir, String name) {
					name = name.toLowerCase();
					return name.endsWith(".java") || name.endsWith(".class");
				}
			});
			packageCommand = "jar cf " + jarName;
			for(int i = 0; i < filenames.length; i++)
				packageCommand += " -C " + basePath + " " + filenames[i];
		}

		if(!system(packageCommand)){
			log("Error packaging file using jar command.");
			return null;
		}
		
		return jarName;
	}

	private String getClasspath() {
		return ServerConfig.getBasePath() + "fruitwar.jar";
	}


	private boolean system(String cmd){
		int ret = -1;
		PrintWriter currentOutput = disableOut ? null : out;
		try {
			ret = SysCmd.exec(new SysCmd.CmdWrapper(cmd, currentOutput, err));
		} catch (Exception e) {
			Logger.exception(e);
		} 
		
		if(ret != 0){
			log("Error execute command. ret=" + ret + ". cmd=" + cmd);
			return false;
		}
		return true;
	}
	
	private void log(String msg){
		if(out != null)
			out.println(msg);
		Logger.log(msg);
	}
	
	public static void main(String[] args) throws IOException{
		RobotJarUtil u = new RobotJarUtil(null, null);
		
		//String s = "public class RandomRobot implements IFruitWarRobot {\r";
		//u.identifyRobotClassName(new BufferedReader(new StringReader(s)));
		u.covertJavaSourceToJar("d:/fruitwar/upload/temp/MyTest1.java");
	}
}
