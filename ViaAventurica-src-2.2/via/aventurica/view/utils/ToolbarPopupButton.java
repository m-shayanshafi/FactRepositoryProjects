package via.aventurica.view.utils;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

// @author  santhosh kumar - santhosh@in.fiorano.com 
public  class ToolbarPopupButton extends ToolbarButton 
                     implements ChangeListener, PopupMenuListener, ActionListener, PropertyChangeListener{ 
    private static final long serialVersionUID = 1L;
	private final JButton mainButton = this; 
    private final JButton arrowButton = new JButton(AppIcons._DROPDOWN_ARROW.ICON) { private static final long serialVersionUID = 1L;
	public Dimension getMaximumSize(){ return new Dimension(12,204); } public boolean hasFocus(){ return false; } }; 
    private final JPopupMenu buttonPopup = new JPopupMenu(); 
    private boolean popupVisible = false; 
 
    public ToolbarPopupButton(ExtendedWorksheetAction defaultAction, ExtendedWorksheetAction...otherActions){
    	super(defaultAction);
    	for(ExtendedWorksheetAction a : otherActions) { 
    		buttonPopup.add(a); 
    	}
    	
 
        mainButton.getModel().addChangeListener(this); 
        arrowButton.getModel().addChangeListener(this); 
        arrowButton.addActionListener(this); 
        arrowButton.setMargin(new Insets(3, 0, 3, 0)); 
        mainButton.addPropertyChangeListener("enabled", this); //NOI18N 
    } 
 
    /*------------------------------[ PropertyChangeListener ]---------------------------------------------------*/ 
 
    public void propertyChange(PropertyChangeEvent evt){ 
        arrowButton.setEnabled(mainButton.isEnabled()); 
    } 
 
    /*------------------------------[ ChangeListener ]---------------------------------------------------*/ 
 
    public void stateChanged(ChangeEvent e){ 
        if(e.getSource()==mainButton.getModel()){ 
            if(popupVisible && !mainButton.getModel().isRollover()){ 
                mainButton.getModel().setRollover(true); 
                return; 
            } 
            arrowButton.getModel().setRollover(mainButton.getModel().isRollover()); 
            arrowButton.setSelected(mainButton.getModel().isArmed() && mainButton.getModel().isPressed()); 
        }else{ 
            if(popupVisible && !arrowButton.getModel().isSelected()){ 
                arrowButton.getModel().setSelected(true); 
                return; 
            } 
            mainButton.getModel().setRollover(arrowButton.getModel().isRollover()); 
        } 
    } 
 
    /*------------------------------[ ActionListener ]---------------------------------------------------*/ 
 
    public void actionPerformed(ActionEvent ae){ 
         JPopupMenu popup = getPopupMenu(); 
         popup.addPopupMenuListener(this); 
         popup.show(mainButton, 0, mainButton.getHeight()); 
     } 
 
    /*------------------------------[ PopupMenuListener ]---------------------------------------------------*/ 
 
    public void popupMenuWillBecomeVisible(PopupMenuEvent e){ 
        popupVisible = true; 
        mainButton.getModel().setRollover(true); 
        arrowButton.getModel().setSelected(true); 
    } 
 
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e){ 
        popupVisible = false; 
 
        mainButton.getModel().setRollover(false); 
        arrowButton.getModel().setSelected(false); 
        ((JPopupMenu)e.getSource()).removePopupMenuListener(this); // act as good programmer :)
    } 
 
    public void popupMenuCanceled(PopupMenuEvent e){ 
        popupVisible = false; 
    } 
 
    /*------------------------------[ Other Methods ]---------------------------------------------------*/ 
 
    protected final JPopupMenu getPopupMenu() {
    	return buttonPopup; 
    }
 
    public JButton addToToolBar(JToolBar toolbar){ 
        toolbar.add(mainButton); 
        toolbar.add(arrowButton); 
        return mainButton; 
    } 
} 