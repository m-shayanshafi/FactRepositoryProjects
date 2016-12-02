package essentials;

import java.io.*;
import java.util.*;

public class OptionsReader {
	File options = new File("Options.txt");
	public OptionsReader(){
	}
	public boolean[] getOptions(){
		boolean[] options = new boolean[5];
		String option = null;
		Scanner read = null;
		try{
			read = new Scanner(this.options);
		}
		catch(FileNotFoundException e){
			System.err.format("no file found");
		}
		catch(NoSuchElementException e){
			System.err.format("line not found");
		}
		for(byte i = 0; i < 2; i++)
			option = read.nextLine();
		options[0] = Boolean.parseBoolean(option);
		for(byte i = 0; i < 2; i++)
			option = read.nextLine();
		options[1] = Boolean.parseBoolean(option);
		for(byte i = 0; i < 2; i++)
			option = read.nextLine();
		options[2] = Boolean.parseBoolean(option);
		for(byte i = 0; i < 2; i++)
			option = read.nextLine();
		options[3] = Boolean.parseBoolean(option);
		for(byte i = 0; i < 2; i++)
			option = read.nextLine();
		options[4] = Boolean.parseBoolean(option);
		if(read != null)
			read.close();
		return options;
	}
	public byte getDataType(){
		Scanner read = null;
		try{
			read = new Scanner(this.options);
		}
		catch(FileNotFoundException e){
			System.err.format("no file found");
		}
		catch(NoSuchElementException e){
			System.err.format("line not found");
		}
		String option = null;
		for(byte i = 0; i < 12; i++)
			option = read.nextLine();
		byte dataType = Byte.parseByte(option);
		if(read != null)
			read.close();
		return dataType;
	}
	public short getGC(){
		Scanner read = null;
		try{
			read = new Scanner(this.options);
		}
		catch(FileNotFoundException e){
			System.err.format("no file found");
		}
		catch(NoSuchElementException e){
			System.err.format("line not found");
		}
		String option = null;
		for(byte i = 0; i < 14; i++)
			option = read.nextLine();
		short GC = Short.parseShort(option);
		if(read != null)
			read.close();
		return GC;
	}
	public void writeToOptions(boolean showCoordinates, boolean enableCheats, boolean isObserver, boolean isMusic, boolean isLogging, byte dataType){
		Scanner read = null;
		try{
			read = new Scanner(this.options);
		}
		catch(FileNotFoundException e){
			System.err.format("no file found");
		}
		catch(NoSuchElementException e){
			System.err.format("line not found");
		}
		String[] file = new String[14];
		for(byte i = 0; i < 14; i++)
			file[i] = read.nextLine();
		file[1] = String.valueOf(showCoordinates);
		file[3] = String.valueOf(enableCheats);
		file[5] = String.valueOf(isObserver);
		file[7] = String.valueOf(isMusic);
		file[9] = String.valueOf(isLogging);
		file[11] = String.valueOf(dataType);
		if(read != null)
			read.close();
		try {
			System.gc();
			options.delete();
			File NewOptions = new File("Options.txt");
			NewOptions.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter("Options.txt", true));
			for(byte i = 0; i < 14; i++){
				bw.write(file[i]);
				bw.newLine();
			}
			bw.close(); 
		} catch (IOException e) {
			System.err.format("creating new file failed");
		}
		
	}
}
