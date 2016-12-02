package infraestrutura.grafico;

import javax.swing.*;

/**
 * O NullRepaintManager � um RepaintManager que n�o faz nenhum repaint.
 * � �til quando toda a renderiza��o � feita manualmente pela aplica��o.
 *
 * @author David Buzatto
 */
public class NullRepaintManager extends RepaintManager {
    
    /**
     * Instala o NullRepaintManager.
     */
    public static void install() {
        RepaintManager repaintManager = new NullRepaintManager();
        repaintManager.setDoubleBufferingEnabled( false );
        RepaintManager.setCurrentManager( repaintManager );
    }
    
    
    public void addInvalidComponent( JComponent c ) {
        // n�o faz nada
    }
    
    
    public void addDirtyRegion( JComponent c, int x, int y,
            int w, int h ) {
        // n�o faz nada
    }
    
    
    public void markCompletelyDirty( JComponent c ) {
        // n�o faz nada
    }
    
    
    public void paintDirtyRegions() {
        // n�o faz nada
    }
    
}