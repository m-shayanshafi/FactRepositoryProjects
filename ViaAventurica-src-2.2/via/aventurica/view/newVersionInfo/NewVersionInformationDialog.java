package via.aventurica.view.newVersionInfo;

import java.awt.BorderLayout;
import java.text.SimpleDateFormat;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import via.aventurica.ViaAventurica;
import via.aventurica.io.WebUpdateNotifier.NewVersionInfo;
import via.aventurica.view.utils.FormEditException;
import via.aventurica.view.utils.GenericWizard;

public class NewVersionInformationDialog extends GenericWizard<NewVersionInfo> {
	private final static long serialVersionUID = 1L;

	public NewVersionInformationDialog(NewVersionInfo info) { 
		super(info, "ViaAventurica Update", "Eine neuere ViaAventurica Version steht zum Download<br>unter "+ViaAventurica.PROJECT_WEBSITE+" bereit.", 600, 320, 1, false, true );
		setOkButtonText("Zur Website"); 
		setCancelButtonText("Schlieﬂen"); 
		
		setVisible(true); 
	}
	
	@Override
	protected JComponent getForm(int pageNumber) {
		NewVersionInfo newVersion = getUserData(); 
		JPanel pnl = new JPanel(new BorderLayout(4,4)); 
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy 'um' HH:mm:ss 'Uhr'"); 
		JEditorPane newVersionDescription = new JEditorPane("text/html", "<html><big><b>"+newVersion.getTitle()+"</big></b><br><i>Quelle: "+ViaAventurica.PROJECT_WEBSITE+"<br>Vom "+format.format(newVersion.getDate().getTime())+"</i><br><hr>"+newVersion.getDescription()+"</html>");
		newVersionDescription.setCaretPosition(0); 
		newVersionDescription.setEditable(false); 
		
		pnl.add(new JScrollPane(newVersionDescription)); 
		return pnl; 
	}
	
	@Override
	protected void okButtonPressed() throws FormEditException {
		// TODO: Zur Website gehen.
		// nicht die super methode aufrufen: Das Formular soll nicht disposed werden!
		// super.okButtonPressed();
	}
}
