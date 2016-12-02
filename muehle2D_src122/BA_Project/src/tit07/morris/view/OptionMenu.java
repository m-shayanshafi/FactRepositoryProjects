package tit07.morris.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.io.IOException;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import tit07.morris.model.Configurateable;
import tit07.morris.model.config.GameStyle;
import tit07.morris.model.config.PlayerInput;
import net.miginfocom.swing.MigLayout;


/**
 * Die Klasse OptionMenu implementiert die GUI des Optionsmen�s
 */
public class OptionMenu extends JDialog {

    /** Referenz auf das Model */
    private Configurateable model;

    /** Referenz auf den Controller */
    private Reactable       controller;

    /** �bernehmen Button */
    private JButton         applyButton;

    /**
     * Variable, um festzustellen, ob openOptionMenu durch den Standard-Button
     * aufgerufen wurde
     */
    private boolean         isCallfromStandardButton = false;

    /** Deklaration der Tabbar */
    private JTabbedPane     tabBar;

    /** Container f�r die Grafikoptionen */
    private JPanel          panelGraphic;

    /** Container f�r die Spieleroptionen */
    private JPanel          panelPlayer;

    /** Container f�r die Buttons */
    private JPanel          panelButtons;

    /** ComboBox f�r die Stylewahl */
    private JComboBox       chosenStyle;

    /** Slider f�r die Animationsgeschwindigkeit */
    private JSlider         animationSpeedSlider;

    /** Textfeld f�r den wei�en Spielername */
    private JTextField      whiteName;

    /** Textfeld f�r den schwarzen Spielername */
    private JTextField      blackName;

    /** ComboBox f�r die wei�e KI-Auswahl */
    private JComboBox       chosenAIWhite;

    /** ComboBox f�r die schwarze KI-Auswahl */
    private JComboBox       chosenAIBlack;

    /** Auswahlknopf f�r wei�en menschlichen Spieler */
    private JRadioButton    radioHumanWhite;

    /** Auswahlknopf f�r schwarzen menschlichen Spieler */
    private JRadioButton    radioHumanBlack;

    /** Auswahlknopf f�r wei�en KI-Spieler */
    private JRadioButton    radioAIWhite;

    /** Auswahlknopf f�r schwarzen KI-Spieler */
    private JRadioButton    radioAIBlack;


    /**
     * Erzeugt die Oberfl�che des Optionsmen�s, welches am Anfang unsichtbar
     * ist.
     * 
     * @param model Referenz auf das Model
     * @param controller Referenz auf den Controller
     */
    public OptionMenu( Configurateable model, Reactable controller ) {

        super();
        this.model = model;
        this.controller = controller;

        /* Optionsmen� unsichtbar machen */
        this.closeOptionMenu();

        /* Feste Gr��e f�r Optionsfenster, nicht mehr skalierbar */
        this.setResizable( false );

        /* Legt die Gr��e des Optionsmen�s fest */
        this.setSize( 400, 300 );

        /* Macht das Options-Men� Modal */
        this.setModal( true );

        /* Setzt den Titel des Optionsmen�s */
        this.setTitle( Messages.getString("OptionMenu.options") ); //$NON-NLS-1$

        /* Setzt das Layout */
        this.setLayout( new BorderLayout() );

        /* Tab-Panel erstellen */
        this.tabBar = new JTabbedPane();

        /* Initialisierung der Grafik-Container */
        this.panelGraphic = new JPanel( new MigLayout( "", //$NON-NLS-1$
                                                       "[]15[][]", //$NON-NLS-1$
                                                       "10[][]40[]10[]" ) ); //$NON-NLS-1$
        this.panelPlayer = new JPanel( new MigLayout( "CENTER", //$NON-NLS-1$
                                                      "[][]70[][]", //$NON-NLS-1$
                                                      "[]10[]30[][][]" ) ); //$NON-NLS-1$

        /* Container mit Inhalt f�llen */
        this.fillPanelGraphic();
        this.fillPanelPlayer();

        /* F�ge die entsprechenden Tab-Reiter hinzu */
        this.tabBar.addTab( Messages.getString("OptionMenu.graphic"), panelGraphic ); //$NON-NLS-1$
        this.tabBar.addTab( Messages.getString("OptionMenu.player"), panelPlayer ); //$NON-NLS-1$

        /* F�ge TabBar in obere h�lfte des Optionsfenster ein */
        this.add( BorderLayout.CENTER, tabBar );
        this.panelButtons = new JPanel( new FlowLayout( FlowLayout.CENTER ),
                                        false );

        /* Erzeugen des OK-Buttons */
        JButton button_ok = new JButton( Messages.getString("OptionMenu.ok") ); //$NON-NLS-1$
        button_ok.setActionCommand( ActionCommand.OK );
        button_ok.setPreferredSize( new Dimension( 50,
                                                   20 ) );
        button_ok.addActionListener( controller );
        this.panelButtons.add( button_ok );

        /* Erzeugen des Abbrechen-Buttons */
        JButton button_abbrechen = new JButton( Messages.getString("OptionMenu.cancel") ); //$NON-NLS-1$
        button_abbrechen.setPreferredSize( new Dimension( 100,
                                                          20 ) );
        button_abbrechen.setActionCommand( ActionCommand.CANCEL );
        button_abbrechen.addActionListener( controller );
        this.panelButtons.add( button_abbrechen );

        /* Erzeugen des Standard-Buttons */
        JButton button_standard = new JButton( Messages.getString("OptionMenu.standard") ); //$NON-NLS-1$
        button_standard.setPreferredSize( new Dimension( 90,
                                                         20 ) );
        button_standard.setActionCommand( ActionCommand.DEFAULT );
        button_standard.addActionListener( controller );
        this.panelButtons.add( button_standard );

        /* Erzeugen des �bernehmen-Buttons */
        this.applyButton = new JButton( Messages.getString("OptionMenu.apply") ); //$NON-NLS-1$
        this.applyButton.setPreferredSize( new Dimension( 110,
                                                          20 ) );
        this.applyButton.setActionCommand( ActionCommand.APPLY );
        this.applyButton.addActionListener( controller );
        this.panelButtons.add( applyButton );
        this.applyButton.setEnabled( false );

        /* F�ge dem unteren Teil des Optionsmen�s den Buttoncontainer hinzu */
        this.add( BorderLayout.SOUTH, panelButtons );

        /* Namenszuweisung der Tab-Reiter */
        this.panelGraphic.setName( "graphicOption" ); //$NON-NLS-1$
        this.panelPlayer.setName( "playerOption" ); //$NON-NLS-1$
    }

    /**
     * Schlie�t das Optionsmen�, indem es versteckt wird
     */
    public void closeOptionMenu() {

        this.setVisible( false );
    }

    /**
     * �ffnet das Optionsmen�, indem es sichtbar wird und aktiviert den
     * entsprechenden Tab-Reiter
     * 
     * @param activeTab Zu aktivierender Tab-Reiter
     */
    public void openOptionMenu( String activeTab ) {

        /* Parameter auf der Config laden */
        this.loadParameters();

        /* Entsprechenden Tab-Reiter aktivieren */
        if( activeTab.equals( ActionCommand.PLAYER_OPTION ) ) {
            this.tabBar.setSelectedComponent( this.panelPlayer );
        }
        else {
            this.tabBar.setSelectedComponent( this.panelGraphic );
        }

        /*
         * �bernehmen-Button aktivieren, wenn Methode vom Standard-Button
         * aufgerufen wurde
         */
        if( this.isCallfromStandardButton ) {
            this.applyButton.setEnabled( true );
        }
        else {
            this.applyButton.setEnabled( false );
        }

        /* Setzen der Radio-Buttons */
        if( this.radioHumanWhite.isSelected() == true ) {
            this.chosenAIWhite.setEnabled( false );
        }
        else {
            this.chosenAIWhite.setEnabled( true );
        }
        if( this.radioHumanBlack.isSelected() == true ) {
            this.chosenAIBlack.setEnabled( false );
        }
        else {
            this.chosenAIBlack.setEnabled( true );
        }
        if( this.radioHumanWhite.isSelected() == true ) {
            this.whiteName.setEnabled( true );
        }
        else
            this.whiteName.setEnabled( false );

        if( this.radioHumanBlack.isSelected() == true ) {
            this.blackName.setEnabled( true );
        }
        else
            this.blackName.setEnabled( false );

        /* Container aktualisieren */
        this.panelPlayer.repaint();

        /* Mache Optionsmen� sichtbar */
        this.setVisible( true );
    }

    /**
     * F�llt Container f�r die Grafikoptionen mit Inhalt
     */
    public void fillPanelGraphic() {

        /* Style-Label */
        JLabel styleLabel = new JLabel( Messages.getString("OptionMenu.style") ); //$NON-NLS-1$

        /* Animationsgeschwindigkeit-Label */
        JLabel animationSpeedLabel = new JLabel( Messages.getString("OptionMenu.ani_speed") ); //$NON-NLS-1$

        /* String Array mit den Namen der verschiedenen Styles */
        String[] gameStyles = {
                Messages.getString("OptionMenu.classic"), //$NON-NLS-1$
                Messages.getString("OptionMenu.modern"), //$NON-NLS-1$
                Messages.getString("OptionMenu.crazy"), //$NON-NLS-1$
                Messages.getString("OptionMenu.matrix"), //$NON-NLS-1$
                Messages.getString("OptionMenu.hell"), //$NON-NLS-1$
                Messages.getString("OptionMenu.heaven") }; //$NON-NLS-1$

        /* ComboBox f�r die GameStyles erstellen */
        this.chosenStyle = new JComboBox( gameStyles );
        this.chosenStyle.setPreferredSize( new Dimension( 300,
                                                          55 ) );

        /* Slider f�r die Animationsgeschwindigkeit */
        this.animationSpeedSlider = new JSlider( 1,
                                                 10 );
        this.animationSpeedSlider.setPaintTicks( true );
        this.animationSpeedSlider.setPaintLabels( true );
        this.animationSpeedSlider.setMajorTickSpacing( 1 );
        this.animationSpeedSlider.setSnapToTicks( true );

        /* Componenten in den Container einf�gen */
        this.panelGraphic.add( styleLabel, "cell 0 1 1 1" ); //$NON-NLS-1$
        this.panelGraphic.add( this.chosenStyle, "cell 1 1 2 1" ); //$NON-NLS-1$
        this.panelGraphic.add( animationSpeedLabel, "cell 0 3 2 1" ); //$NON-NLS-1$
        this.panelGraphic.add( this.animationSpeedSlider, "cell 1 4 2 1" ); //$NON-NLS-1$

        /* Listener hinzuf�gen */
        this.chosenStyle.setActionCommand( ActionCommand.ENABLE_APPLY );
        this.chosenStyle.addActionListener( this.controller );
        this.animationSpeedSlider.addChangeListener( this.controller );

        /* Container aktualisieren */
        this.panelGraphic.repaint();
    }

    /**
     * F�llt Container f�r die Spieleroptionen mit Inhalt
     */
    public void fillPanelPlayer() {

        /* Erstelle die 2 Labels f�r schwarz und wei� */
        JLabel whiteLabel = new JLabel( Messages.getString("OptionMenu.player_white") ); //$NON-NLS-1$
        JLabel blackLabel = new JLabel( Messages.getString("OptionMenu.player_black") ); //$NON-NLS-1$

        /* Erstelle Label f�r Spielerauswahl */
        JLabel choiceWhite = new JLabel( Messages.getString("OptionMenu.who_shall_play") ); //$NON-NLS-1$
        JLabel choiceBlack = new JLabel( Messages.getString("OptionMenu.who_shall_play") ); //$NON-NLS-1$

        /* Erstelle zwei Textfelder f�r die Namenseingabe (max.16 Zeichen) */
        this.whiteName = new JTextField( "", //$NON-NLS-1$
                                         16 );
        this.blackName = new JTextField( "", //$NON-NLS-1$
                                         16 );

        /* Erstelle zwei DropDown Men�s f�r die KI */
        String[] entries = { Messages.getString("OptionMenu.very_easy"), Messages.getString("OptionMenu.easy"), Messages.getString("OptionMenu.normal") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        this.chosenAIWhite = new JComboBox( entries );
        this.chosenAIWhite.setActionCommand( ActionCommand.ENABLE_APPLY );
        this.chosenAIWhite.addActionListener( controller );
        this.chosenAIBlack = new JComboBox( entries );
        this.chosenAIBlack.setActionCommand( ActionCommand.ENABLE_APPLY );
        this.chosenAIBlack.addActionListener( controller );

        /* Erstelle die Radioboxen f�r die Spielerauswahl */
        this.radioHumanWhite = new JRadioButton( Messages.getString("OptionMenu.human"), //$NON-NLS-1$
                                                 true );
        this.radioHumanBlack = new JRadioButton( Messages.getString("OptionMenu.human"), //$NON-NLS-1$
                                                 true );
        this.radioAIWhite = new JRadioButton( Messages.getString("OptionMenu.ai"), //$NON-NLS-1$
                                              false );
        this.radioAIBlack = new JRadioButton( Messages.getString("OptionMenu.ai"), //$NON-NLS-1$
                                              false );

        /* Erstellen der Gruppen f�r die Radioboxen */
        ButtonGroup whiteGroup = new ButtonGroup();
        whiteGroup.add( this.radioHumanWhite );
        whiteGroup.add( this.radioAIWhite );
        ButtonGroup blackGroup = new ButtonGroup();
        blackGroup.add( this.radioHumanBlack );
        blackGroup.add( this.radioAIBlack );

        /* Componenten in den Container einf�gen */
        this.panelPlayer.add( whiteLabel, "cell 0 0" ); //$NON-NLS-1$
        this.panelPlayer.add( blackLabel, "cell 2 0" ); //$NON-NLS-1$
        this.panelPlayer.add( this.whiteName, "cell 0 1 " ); //$NON-NLS-1$
        this.panelPlayer.add( this.blackName, "cell 2 1 " ); //$NON-NLS-1$
        this.panelPlayer.add( choiceWhite, "cell 0 2" ); //$NON-NLS-1$
        this.panelPlayer.add( choiceBlack, "cell 2 2" ); //$NON-NLS-1$
        this.panelPlayer.add( this.radioHumanWhite, "cell 0 3 " ); //$NON-NLS-1$
        this.panelPlayer.add( this.radioHumanBlack, "cell 2 3" ); //$NON-NLS-1$
        this.panelPlayer.add( this.radioAIWhite, "cell 0 4 " ); //$NON-NLS-1$
        this.panelPlayer.add( this.radioAIBlack, "cell 2 4 " ); //$NON-NLS-1$
        this.panelPlayer.add( this.chosenAIWhite, "cell 0 5 " ); //$NON-NLS-1$
        this.panelPlayer.add( this.chosenAIBlack, "cell 2 5 " ); //$NON-NLS-1$

        /* Listener hinzuf�gen */
        this.whiteName.getDocument().addDocumentListener( this.controller );
        this.blackName.getDocument().addDocumentListener( this.controller );
        this.chosenAIWhite.addActionListener( this.controller );
        this.chosenAIBlack.addActionListener( this.controller );
        this.radioHumanWhite.setActionCommand( ActionCommand.HUMAN_WHITE );
        this.radioHumanBlack.setActionCommand( ActionCommand.HUMAN_BLACK );
        this.radioHumanWhite.addActionListener( this.controller );
        this.radioHumanBlack.addActionListener( this.controller );
        this.radioAIWhite.setActionCommand( ActionCommand.AI_WHITE );
        this.radioAIBlack.setActionCommand( ActionCommand.AI_BLACK );
        this.radioAIWhite.addActionListener( this.controller );
        this.radioAIBlack.addActionListener( this.controller );

        /* Container aktualisieren */
        this.panelPlayer.repaint();
    }

    /**
     * Abgleich des Optionsmen� mit der aktuellen Spielekonfiguration.
     */
    public void loadParameters() {

        /* Grafikeinstellungen */
        String stylename = model.getConfig().getGameStyle().getStyleName();
        if( stylename.equals( "CLASSIC" ) ) { //$NON-NLS-1$
            this.chosenStyle.setSelectedItem( Messages.getString("OptionMenu.classic") ); //$NON-NLS-1$
        }
        else if( stylename.equals( "MODERN" ) ) { //$NON-NLS-1$
            this.chosenStyle.setSelectedItem( Messages.getString("OptionMenu.modern") ); //$NON-NLS-1$
        }
        else if( stylename.equals( "CRAZY" ) ) { //$NON-NLS-1$
            this.chosenStyle.setSelectedItem( Messages.getString("OptionMenu.crazy") ); //$NON-NLS-1$
        }
        else if( stylename.equals( "MATRIX" ) ) { //$NON-NLS-1$
            this.chosenStyle.setSelectedItem( Messages.getString("OptionMenu.matrix") ); //$NON-NLS-1$
        }
        else if( stylename.equals( "HEAVEN" ) ) { //$NON-NLS-1$
            this.chosenStyle.setSelectedItem( Messages.getString("OptionMenu.heaven") ); //$NON-NLS-1$
        }
        else if( stylename.equals( "HELL" ) ) { //$NON-NLS-1$
            this.chosenStyle.setSelectedItem( Messages.getString("OptionMenu.hell") ); //$NON-NLS-1$
        }
        this.animationSpeedSlider.setValue( model.getConfig()
                                                 .getAnimationSpeed() );
        this.panelGraphic.repaint();

        /* Spielereinstellungen */
        this.whiteName.setText( model.getConfig().getWhiteNameTextField() );
        this.blackName.setText( model.getConfig().getBlackNameTextField() );
        PlayerInput whiteInput = model.getConfig().getWhiteInput();
        PlayerInput blackInput = model.getConfig().getBlackInput();

        /* Wei�er Spieler */
        if( whiteInput == PlayerInput.HUMAN ) {
            this.chosenAIWhite.setSelectedItem( model.getConfig().getKiWhite() );
            this.radioHumanWhite.setSelected( true );
        }
        else {
            this.radioAIWhite.setSelected( true );
        }
        this.chosenAIWhite.setSelectedItem( model.getConfig().getKiWhite() );

        /* Schwarzer Spieler */
        if( blackInput == PlayerInput.HUMAN ) {
            this.chosenAIBlack.setSelectedItem( model.getConfig().getKiBlack() );
            this.radioHumanBlack.setSelected( true );
        }
        else {
            this.radioAIBlack.setSelected( true );
        }
        this.chosenAIBlack.setSelectedItem( model.getConfig().getKiBlack() );

        this.panelPlayer.repaint();
    }

    /**
     * Schreibt die aktuellen Einstellungen vom Optionsmen� in die Config
     */
    public void saveParameters() throws Exception {

        /* Grafikeinstellungen */
        String selectedStyle = (String) chosenStyle.getSelectedItem();

        if( selectedStyle.equals( Messages.getString("OptionMenu.classic") ) ) { //$NON-NLS-1$
            this.model.getConfig().setGameStyle( GameStyle.getClassicStyle() );
        }
        else if( selectedStyle.equals( Messages.getString("OptionMenu.modern") ) ) { //$NON-NLS-1$
            this.model.getConfig().setGameStyle( GameStyle.getModernStyle() );
        }
        else if( selectedStyle.equals( Messages.getString("OptionMenu.crazy") ) ) { //$NON-NLS-1$
            this.model.getConfig().setGameStyle( GameStyle.getCrazyStyle() );
        }
        else if( selectedStyle.equals( Messages.getString("OptionMenu.hell") ) ) { //$NON-NLS-1$
            this.model.getConfig().setGameStyle( GameStyle.getHellStyle() );
        }
        else if( selectedStyle.equals( Messages.getString("OptionMenu.heaven") ) ) { //$NON-NLS-1$
            this.model.getConfig().setGameStyle( GameStyle.getHeavenStyle() );
        }
        else if( selectedStyle.equals( Messages.getString("OptionMenu.matrix") ) ) { //$NON-NLS-1$
            this.model.getConfig().setGameStyle( GameStyle.getMatrixStyle() );
        }
        this.model.getConfig()
                  .setAnimationSpeed( animationSpeedSlider.getValue() );

        /* Spielereinstellungen */
        if( this.radioHumanWhite.isSelected() ) {
            this.model.getConfig().setWhiteInput( PlayerInput.HUMAN );
        }
        else {
            String choisenAI = (String) chosenAIWhite.getSelectedItem();
            if( choisenAI.equals( Messages.getString("OptionMenu.very_easy") ) ) { //$NON-NLS-1$
                this.model.getConfig()
                          .setWhiteInput( PlayerInput.CPU_VERY_WEAK );
                this.model.getConfig().setKiWhite( choisenAI );
            }
            else if( choisenAI.equals( Messages.getString("OptionMenu.easy") ) ) { //$NON-NLS-1$
                this.model.getConfig().setWhiteInput( PlayerInput.CPU_WEAK );
                this.model.getConfig().setKiWhite( choisenAI );
            }
            else if( choisenAI.equals( Messages.getString("OptionMenu.normal") ) ) { //$NON-NLS-1$
                this.model.getConfig().setWhiteInput( PlayerInput.CPU_NORMAL );
                this.model.getConfig().setKiWhite( choisenAI );
            }
        }
        if( this.radioHumanBlack.isSelected() ) {
            this.model.getConfig().setBlackInput( PlayerInput.HUMAN );
        }
        else {
            String ki_auswahl = (String) chosenAIBlack.getSelectedItem();
            if( ki_auswahl.equals( Messages.getString("OptionMenu.very_easy") ) ) { //$NON-NLS-1$
                this.model.getConfig()
                          .setBlackInput( PlayerInput.CPU_VERY_WEAK );
                this.model.getConfig().setKiBlack( ki_auswahl );
            }
            else if( ki_auswahl.equals( Messages.getString("OptionMenu.easy") ) ) { //$NON-NLS-1$
                this.model.getConfig().setBlackInput( PlayerInput.CPU_WEAK );
                this.model.getConfig().setKiBlack( ki_auswahl );
            }
            else if( ki_auswahl.equals( Messages.getString("OptionMenu.normal") ) ) { //$NON-NLS-1$
                this.model.getConfig().setBlackInput( PlayerInput.CPU_NORMAL );
                this.model.getConfig().setKiBlack( ki_auswahl );
            }
        }
        try {
            this.model.getConfig().saveToDisk();
        }
        catch( IOException e ) {
            throw new Exception( Messages.getString("OptionMenu.conf_not_saved") ); //$NON-NLS-1$
        }
    }

    /**
     * Gibt den Namen des wei�en Spielers zur�ck
     * 
     * @return Namen des wei�en Spielers
     */
    public JTextField getWhiteName() {

        return this.whiteName;
    }

    /**
     * Setzt den Namen des wei�en Spielers
     * 
     * @param whiteName Name des wei�en Spielers
     */
    public void setWhiteName( JTextField whiteName ) {

        this.whiteName = whiteName;
    }

    /**
     * Gibt den Namen des schwarzen Spielers
     * 
     * @return Name des schwarzen Spielers
     */
    public JTextField getBlackName() {

        return this.blackName;
    }

    /**
     * Setzt den Namen des schwarzen Spielers
     * 
     * @param blackName Name des schwarzen Spielers
     */
    public void setBlackName( JTextField blackName ) {

        this.blackName = blackName;
    }

    /**
     * Gibt eine Referenz auf den �bernehmen-Button zur�ck
     * 
     * @return Referenz auf den �bernehmen-Button
     */
    public JButton getApplyButton() {

        return this.applyButton;
    }

    /**
     * Gibt die Auswahl-Box f�r die wei�e KI zur�ck
     * 
     * @return Auswahl-Box f�r die wei�e KI
     */
    public JComboBox getChosenAIWhite() {

        return this.chosenAIWhite;
    }

    /**
     * Gibt die Auswahl-Box f�r die schwarze KI zur�ck
     * 
     * @return Auswahl-Box f�r die schwarze KI
     */
    public JComboBox getChosenAIBlack() {

        return this.chosenAIBlack;
    }

    /**
     * Gibt die Tab-Bar zur�ck
     * 
     * @return Referenz auf die Tab-Bar
     */
    public JTabbedPane getTabBar() {

        return this.tabBar;
    }

    /**
     * Setzt den Flag, ob das Optionsmen� vom Standard-Button aufgerufen wurde
     * 
     * @param isCallFromStandardButton True, wenn es vom Standard-Button
     *            aufgerufen wurde.
     */
    public void setIsCallFromStandardButton( boolean isCallFromStandardButton ) {

        this.isCallfromStandardButton = isCallFromStandardButton;
    }
}
