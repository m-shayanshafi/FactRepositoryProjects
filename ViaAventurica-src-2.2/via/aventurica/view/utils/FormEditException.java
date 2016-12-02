package via.aventurica.view.utils;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JOptionPane;

public class FormEditException extends Exception { 
	private static final long serialVersionUID = 1L;
	private int returnToPageNumber; 
	private ArrayList<String> problems = new ArrayList<String>(); 

	public FormEditException(String...problems) { 
		this(-1, problems); 
	}
	
	public FormEditException(int goToForm, String...problems) { 
		returnToPageNumber = goToForm; 
		Collections.addAll(this.problems, problems); 
	}
	
	public void addProblem(String problem) { 
		problems.add(problem); 
	}
	
	public boolean hasProblems() { 
		return problems.size()>0; 
	}
	
	public void setReturnToPageNumber(int returnToPageNumber) {
		this.returnToPageNumber = returnToPageNumber;
	}
	
	public int getReturnToPageNumber() {
		return returnToPageNumber;
	}
	
	public void showMessages(Component parent) { 
		JOptionPane.showMessageDialog(parent, getMessage(), "Fehler bei der Eingabe", JOptionPane.ERROR_MESSAGE); 
	}
	
	
	@Override
	public String getMessage() {
		StringBuffer buff = new StringBuffer("<html>Bei der Eingabe sind folgende Fehler aufgetreten:<ol>");
		for(String problem : problems)
			buff.append("<li>"+problem+"</li>"); 
		buff.append("</ol></html>"); 
		return buff.toString(); 
	}
}