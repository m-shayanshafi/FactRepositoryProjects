package via.aventurica.view.mapcreator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;

import via.aventurica.io.FileCopy;
import via.aventurica.model.map.MapCollection;
import via.aventurica.model.map.MapInfo;
import via.aventurica.model.map.MapLoaderFactory;
import via.aventurica.model.map.MapSetWriter;
import via.aventurica.view.utils.FormEditException;
import via.aventurica.view.utils.GenericWizard;
import via.aventurica.view.utils.JFilePickerButton;
import via.aventurica.view.utils.VerticalLayout;

public class MapCreator extends GenericWizard<MapCollection> {
	private final static long serialVersionUID = 1L;
 
	private final static int PAGE_COUNT = 3; 
	private final List<MapCollection> mapCollections; 
	private JRadioButton useExistingCollection, createNewCollection;
	private JTextField newCollectionTitle, mapTitle, mapCopyright, pixelCount, realDistance, distanceUnit;
	private JList existingCollectionList; 
	private JFilePickerButton mapFilePicker;
	private MeasurementPanel previewPanel; 
	private Rectangle origBounds; 
	private BufferedImage previewImage; 
	
	public MapCreator(List<MapCollection> collections) { 
		super(null, "Karte hinzufügen", "Erstellt eine neue Karte für ViaAventurica", 400, 320, PAGE_COUNT, true, true); 
		this.mapCollections = collections; 
		setResizable(true); 
		
		setOkButtonText("Fertig");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE); 
		setModal(true); 
		origBounds = getBounds(); 
		 
		setVisible(true); 
	}

	private final static int PICK_MAP_PAGE = 0, MAPPING_PAGE = 1, SET_SELECTION_PAGE = 2; 
	
	@Override
	protected void okButtonPressed() throws FormEditException {
	
		// TODO: Validierung wird nicht überprüft. 
		boolean collectionExists = useExistingCollection.isSelected(); 
		MapCollection collectionToEdit = null;
		
		// Lege die Collection an, bzw. prüfe, ob sie angelegt werden kann: 
		if(!collectionExists) {
			String collectionTitle = newCollectionTitle.getText().trim(); 
			
			if(collectionTitle.length()==0)
				throw new FormEditException(SET_SELECTION_PAGE, "Bitte ein Kartenset für diese Karte auswählen"); 

			if(MapCollection.collectionExists(collectionTitle))
				throw new FormEditException(SET_SELECTION_PAGE, "Der Name des Kartensets muss einmalig sein.<br>Es existiert bereits ein Kartenset mit diesem Namen");
			
			if(!MapCollection.isValidCollectionName(collectionTitle))
				throw new FormEditException(SET_SELECTION_PAGE, "Konnte das Kartenset nicht anlegen.<br>Für den Namen des Sets gelten die gleichen Bedingungen wie für Ordnernamen");


			collectionToEdit = new MapCollection(collectionTitle, new File(MapLoaderFactory.MAPS_FOLDER, collectionTitle)); 
		} else { 
			collectionToEdit = (MapCollection)existingCollectionList.getSelectedValue(); 
		}
		
		FormEditException exc = new FormEditException(-1); 
	
		String mapTitle = this.mapTitle.getText().trim();
		if(mapTitle.length()==0) { 
			exc.addProblem("Bitte einen Kartentitel festlegen"); 
		} else {
			for(MapInfo mapInfo : collectionToEdit) {
				if(mapInfo.getTitle().equalsIgnoreCase(mapTitle)) {
					exc.addProblem("Der Name dieser Karte ist in dem Kartenset bereits belegt"); 
					break;
				}
			}
		}
		
		String distanceUnit = this.distanceUnit.getText().trim(); 
		if(distanceUnit.length()==0)
			exc.addProblem("Bitte eine Distanzeinheit (z.B. Meilen) angeben!"); 
		
		if(!isValidDistance(pixelCount.getText()))
			exc.addProblem("Die Pixel Entfernung muss eine Ganze Zahl größer 0 sein"); 
		if(!isValidDistance(realDistance.getText()))
			exc.addProblem("Die Echte Entfernung muss eine Ganze Zahl größer 0 sein"); 
		
		System.err.println(exc.getMessage());
		
		if(exc.hasProblems())
			throw exc; 
		
		if(!collectionExists) {  
			collectionToEdit.getCollectionBaseFolder().mkdir(); 
			mapCollections.add(collectionToEdit); 
		}
	
		
		try { 
			File mapTargetFile = findExistingFile(collectionToEdit.getCollectionBaseFolder(), mapFilePicker.getSelectedFile().getName());
			File mapPreviewFile = findExistingFile(collectionToEdit.getCollectionBaseFolder(), "preview-"+mapTargetFile.getName()+".jpg");
			
			FileCopy.copyFile(mapFilePicker.getSelectedFile(), mapTargetFile);
			generatePreviewImage(); 
			ImageIO.write(previewImage, "jpg", mapPreviewFile);
			
			MapSetWriter.addMap(collectionToEdit, 
					mapTargetFile, 
					mapPreviewFile, 
					mapTitle, 
					mapCopyright.getText(), 
					distanceUnit, 
					Integer.parseInt(pixelCount.getText()), 
					Integer.parseInt(realDistance.getText())); 
			
		} catch(Exception ex) { 
			exc.addProblem(ex.getLocalizedMessage()); 
			ex.printStackTrace(); 
		}
		

		// TODO: Save Collection
		if(exc.hasProblems())
			throw exc; 
		
		

		
		super.okButtonPressed();
	}
	
	private File findExistingFile(File parentFolder, String filename) { 
		File saveFile = new File(parentFolder, filename);
		final String extension = filename.substring(filename.lastIndexOf("."));
		final String name = filename.substring(0, filename.lastIndexOf(".")-1); 
		int counter = 1; 
		while(saveFile.exists()) { 
			saveFile = new File(parentFolder, name+" ("+counter+")"+extension); 
		}
		return saveFile; 
	}
	
	private boolean isValidDistance(final String distanceStr) { 
		try{ 
			int parsedDistance = Integer.parseInt(distanceStr);
			return parsedDistance > 0; 
		} catch(NumberFormatException ex) { 
			return false; 
		}
	}
	
	@Override
	protected JComponent getForm(int pageNumber) {
		switch(pageNumber) {
		case PICK_MAP_PAGE: 
			return getPickMapForm();  
		case MAPPING_PAGE: 
			return getMappingForm();
		case SET_SELECTION_PAGE: 
			return getMapSetSelectionForm(); 
		default:
			System.err.println("Page is missing!");
		}
		return null; 
	}
	
	private JComponent getMappingForm() { 
		JPanel form = new JPanel(new BorderLayout());
		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
		
		pixelCount = new JTextField(5); 
		realDistance = new JTextField(5); 
		distanceUnit = new JTextField(5); 
		previewPanel = new MeasurementPanel(); 
		previewPanel.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseReleased(MouseEvent e) {
				int length = previewPanel.getSelectedLength();
				if(length > 0)
					pixelCount.setText(""+length); 
			}
		}); 
				
		headerPanel.add(pixelCount); 
		headerPanel.add(new JLabel("Pixel entsprechen ")); 
		headerPanel.add(realDistance); 
		headerPanel.add(new JLabel("in der Einheit")); 
		headerPanel.add(distanceUnit); 
				
		form.add(headerPanel, BorderLayout.PAGE_START);
		form.add(new JScrollPane(previewPanel)); 
		
		return form; 
	}
	
	private final void generatePreviewImage() { 
		BufferedImage img = previewPanel.getImage();
		int origWidth = img.getWidth(); 
		int origHeight = img.getHeight(); 
		
		// Sollte auf  1/3 festgelegt bleiben.
		int previewWidth = origWidth/3; 
		int previewHeight = origHeight/3; 
		
		previewImage = new BufferedImage(120, 80, img.getType());
		previewImage.createGraphics().drawImage(img, 0, 0, previewImage.getWidth(), previewImage.getHeight(), 
				previewWidth, previewHeight, 2* previewWidth, 2*previewHeight, null);
	}
	
	@Override
	protected void pageOpened(int pageNumber) {
		if(pageNumber == MAPPING_PAGE) {
			File f = mapFilePicker.getSelectedFile();
			if(f==null) { 
				JOptionPane.showMessageDialog(this, "Bitte zuerst eine Karte wählen!", "Keine Karte gewählt", JOptionPane.WARNING_MESSAGE); 
				lastPage(); 
			}
			previewPanel.loadImage(f.getAbsolutePath(), false);

			Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
			setSize(Math.min(origBounds.width*2, size.width), Math.min(origBounds.height*2, size.height));
		} else { 
			setSize(origBounds.width, origBounds.height); 
		}
	}
	
	private JComponent getPickMapForm() { 
		mapFilePicker = new JFilePickerButton("Bilddateien", ".jpg", ".gif", ".png", ".bmp", ".tif");
		mapTitle = new JTextField(20); 
		mapCopyright = new JTextField(20); 
		
		JPanel form = new JPanel(new VerticalLayout(0, VerticalLayout.LEFT, VerticalLayout.TOP));
		
		form.add(new JLabel("Titel der Karte")); 
		form.add(insetComponents(mapTitle));
		form.add(new JLabel("Bitte wähle die Karte aus:"));
		form.add(insetComponents(mapFilePicker));
		form.add(new JLabel("Copyright der Karte (Optional)")) ;
		form.add(insetComponents(mapCopyright)); 
		return form; 
	}

	
	private JComponent getMapSetSelectionForm() { 
		JPanel mapsetSelection = new JPanel(new VerticalLayout(0, VerticalLayout.LEFT, VerticalLayout.TOP)); 
		//Border leftInsetBorder = BorderFactory.createEmptyBorder(0, 15, 5, 0); 
		
		useExistingCollection = new JRadioButton("Karte in ein vorhandenes Set speichern:", true); 
		createNewCollection = new JRadioButton("Karte in einem neuen Set namens", false);
		existingCollectionList = new JList(mapCollections.toArray());
		
		newCollectionTitle = new JTextField(20); 
				
		existingCollectionList.setVisibleRowCount(4);
		JScrollPane existingCollectionsListScrollpane = new JScrollPane(existingCollectionList);
		 
		//existingCollectionsListScrollpane.setPreferredSize(existingCollectionList.getPreferredSize()); 
		ButtonGroup bg = new ButtonGroup(); 
		bg.add(useExistingCollection);
		bg.add(createNewCollection); 

		
		mapsetSelection.add(new JLabel("In welchem Kartenset soll die neue Karte gespeichert werden?")); 
		mapsetSelection.add(useExistingCollection);
		mapsetSelection.add(insetComponents(existingCollectionsListScrollpane));
		mapsetSelection.add(createNewCollection); 
		mapsetSelection.add(insetComponents(newCollectionTitle, new JLabel("speichern"))); 
		
		ActionListener radioButtonListener = new ActionListener() { 
			
			public void actionPerformed(ActionEvent e) {
				if(e.getSource().equals(createNewCollection)) { 
					newCollectionTitle.setEnabled(true); 
					newCollectionTitle.requestFocus(); 
					existingCollectionList.setEnabled(false); 
				} else if(e.getSource().equals(useExistingCollection)) { 
					newCollectionTitle.setEnabled(false); 
					existingCollectionList.setEnabled(true);
					existingCollectionList.requestFocus();
				}
				
			}
		};
		
		createNewCollection.addActionListener(radioButtonListener); 
		useExistingCollection.addActionListener(radioButtonListener); 
		newCollectionTitle.setEnabled(false); 
		existingCollectionList.setSelectedIndex(0); 
		
		return mapsetSelection; 
	}
	
	private final Border insetBorder = BorderFactory.createEmptyBorder(0, 15, 5, 0); 
	
	private final JComponent insetComponents(JComponent...components) {
		JPanel insetPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		insetPanel.setBorder(insetBorder); 
		for(JComponent c : components)
			insetPanel.add(c); 
		
		return insetPanel; 
	}
	
	
	public static void main(String[] args) {
		try { 
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) { }
		new MapCreator(MapLoaderFactory.listMaps()); 
	}
}
