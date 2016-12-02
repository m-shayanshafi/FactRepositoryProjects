package infraestrutura.input;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;

/**
 * O InputManager gerencia a entrada de teclas e eventos do mouse.
 * Os eventos s�o mapeados para GameActions.
 *
 * @author David Buzatto
 */
public class InputManager implements KeyListener, MouseListener,
    MouseMotionListener, MouseWheelListener {
    
    
    /**
     * Um cursor invis�vel.
     */
    public static final Cursor INVISIBLE_CURSOR =
            Toolkit.getDefaultToolkit().createCustomCursor(
            Toolkit.getDefaultToolkit().getImage( "" ),
            new Point( 0, 0 ),
            "invisible" );
    
    /**
     * C�digos do mouse.
     */
    public static final int MOUSE_MOVE_LEFT = 0;
    public static final int MOUSE_MOVE_RIGHT = 1;
    public static final int MOUSE_MOVE_UP = 2;
    public static final int MOUSE_MOVE_DOWN = 3;
    public static final int MOUSE_WHEEL_UP = 4;
    public static final int MOUSE_WHEEL_DOWN = 5;
    public static final int MOUSE_BUTTON_1 = 6;
    public static final int MOUSE_BUTTON_2 = 7;
    public static final int MOUSE_BUTTON_3 = 8;

    private static final int NUM_MOUSE_CODES = 9;

    
    /**
     * ps c�digos de teclas s�o definidos em java.awt.KeyEvent.
     * a maiortia dos c�digos (com excess�o de alguns raros como "alt graph"
     * s�o menores que 600.
     */
    private static final int NUM_KEY_CODES = 600;

    private GameAction[] keyActions = new GameAction[ NUM_KEY_CODES ];
    private GameAction[] mouseActions = new GameAction[ NUM_MOUSE_CODES ];

    private Point mouseLocation;
    private Point centerLocation;
    private Component comp;
    private Robot robot;
    private boolean isRecentering;
    
    
    /**
     * Cria um novo InputManager que ouve as entradas de um componente 
     * espec�fico.
     */
    public InputManager( Component comp ) {
        this.comp = comp;
        mouseLocation = new Point();
        centerLocation = new Point();

        // registra os ouvintes de tecla e do mouse
        comp.addKeyListener( this );
        comp.addMouseListener( this );
        comp.addMouseMotionListener( this );
        comp.addMouseWheelListener( this );
        
        /*
         * permite a entrada da tecla TAB e outras teclas normalmente usadas
         * pelo focus traversal.
         */
        comp.setFocusTraversalKeysEnabled( false );
    }

    
    /**
     * Configura o cursor no componente do InputManager.
     */
    public void setCursor( Cursor cursor ) {
        comp.setCursor( cursor );
    }

    
    /**
     * Configura quando o modo relativo do mous est� ligado ou n�o.
     * Para o modo relativo do mouse, o cursor fica "trancado" no centro
     * da tela, e somente a mudan�a no movimento do mouse � medida.
     * No modo normal, o mouse fica livre para mover pela a tela.
     */
    public void setRelativeMouseMode( boolean mode ) {
        if ( mode == isRelativeMouseMode() ) {
            return;
        }

        if ( mode ) {
            try {
                robot = new Robot();
                recenterMouse();
            }
            catch ( AWTException ex ) {
                // n�o p�de criar um Robot
                robot = null;
            }
        } else {
            robot = null;
        }
    }

    
    /**
     * Retorna se o modo relativo do mouse est� ligado ou n�o.
     */
    public boolean isRelativeMouseMode() {
        return ( robot != null );
    }

    
    /**
     * Mapeia uma GameAction para uma tecla espec�fica.
     * Os c�digos das telas s�o definidos em java.awt.KeyEvent.
     * Se a tecla j� tiver uma GameAction mapeada a ela, a nova GameAction
     * sobrescreve a mesma.
     */
    public void mapToKey( GameAction gameAction, int keyCode ) {
        keyActions[ keyCode ] = gameAction;
    }

    
    /**
     * Mapeia uma GameAction para uma a��o espec�fica do mouse.
     * Os c�digos do mouse s�o definidos aqui no InputManager
     * (MOUSE_MOVE_LEFT, MOUSE_BUTTON_1, etc). Se a a��o do mouse j� tiver uma 
     * GameAction mapeada a ela, a nova GameAction sobrescrever� a mesma.
     */
    public void mapToMouse( GameAction gameAction, int mouseCode ) {
        mouseActions[ mouseCode ] = gameAction;
    }
    
    
    /**
     * Limpa todas as teclas mapeadas e a��es do mouse para essa GameAction.
     */
    public void clearMap( GameAction gameAction ) {
        for ( int i = 0; i < keyActions.length; i++ ) {
            if ( keyActions[ i ] == gameAction ) {
                keyActions[ i ] = null;
            }
        }

        for ( int i = 0; i < mouseActions.length; i++ ) {
            if ( mouseActions[ i ] == gameAction ) {
                mouseActions[ i ] = null;
            }
        }

        gameAction.reset();
    }

    
    /**
     * Obt�m uma List dos nomes das teclas e a��es do mouse mapeadas para essa 
     * GameAction cada entrada na lista � uma String.
     */
    public List getMaps( GameAction gameCode ) {
        ArrayList< String > list = new ArrayList< String >();

        for ( int i = 0; i < keyActions.length; i++ ) {
            if ( keyActions[ i ] == gameCode ) {
                list.add( getKeyName( i ) );
            }
        }

        for ( int i = 0; i < mouseActions.length; i++ ) {
            if ( mouseActions[ i ] == gameCode ) {
                list.add( getMouseName( i ) );
            }
        }
        return list;
    }

    
    /**
     * Reseta todas as GameAction, ent�o elas ficam em um estado que parece
     * que elas n�o foram pressionadas.
     */
    public void resetAllGameActions() {
        for ( int i = 0; i < keyActions.length; i++ ) {
            if ( keyActions[ i ] != null ) {
                keyActions[ i ].reset();
            }
        }

        for ( int i = 0; i < mouseActions.length; i++ ) {
            if ( mouseActions[ i ] != null ) {
                mouseActions[ i ].reset();
            }
        }
    }

    
    /**
     * Obt�m o nome de um c�digo de tecla.
     */
    public static String getKeyName( int keyCode ) {
        return KeyEvent.getKeyText( keyCode );
    }
    
    
    /**
     * Obt�m o nome de um c�digo do mouse.
     */
    public static String getMouseName( int mouseCode ) {
        switch ( mouseCode ) {
            
            case MOUSE_MOVE_LEFT: 
                return "Mouse Left";
                
            case MOUSE_MOVE_RIGHT: 
                return "Mouse Right";
                
            case MOUSE_MOVE_UP: 
                return "Mouse Up";
                
            case MOUSE_MOVE_DOWN: 
                return "Mouse Down";
                
            case MOUSE_WHEEL_UP: 
                return "Mouse Wheel Up";
                
            case MOUSE_WHEEL_DOWN: 
                return "Mouse Wheel Down";
                
            case MOUSE_BUTTON_1: 
                return "Mouse Button 1";
                
            case MOUSE_BUTTON_2: 
                return "Mouse Button 2";
                
            case MOUSE_BUTTON_3: 
                return "Mouse Button 3";
                
            default: 
                return "Unknown mouse code " + mouseCode;
        }
    }

    
    /**
     * Obt�m a posi��o x do mouse.
     */
    public int getMouseX() {
        return mouseLocation.x;
    }

    
    /**
     * Obt�m a posi��o y do mouse.
     */
    public int getMouseY() {
        return mouseLocation.y;
    }

    
    /**
     * Usa a classe Robot para tentar posicionar o mouse no centro da tela.
     * <p> Note que o uso da classe Robot pode n�o ser poss�vel em todas as 
     * plataformas.
     */
    private synchronized void recenterMouse() {
        if ( robot != null && comp.isShowing() ) {
            centerLocation.x = comp.getWidth() / 2;
            centerLocation.y = comp.getHeight() / 2;
            SwingUtilities.convertPointToScreen( centerLocation, comp );
            isRecentering = true;
            robot.mouseMove( centerLocation.x, centerLocation.y );
        }
    }
    
    
    /**
     * Retorna a GameAction associada ao KeyEvent.
     */
    private GameAction getKeyAction( KeyEvent e ) {
        int keyCode = e.getKeyCode();
        if ( keyCode < keyActions.length ) {
            return keyActions[ keyCode ];
        } else {
            return null;
        }
    }

    
    /**
     * Obt�m o c�digo do mouse para o bot�o especificado no MouseEvent
     */
    public static int getMouseButtonCode( MouseEvent e ) {
         switch ( e.getButton() ) {
             
            case MouseEvent.BUTTON1:
                return MOUSE_BUTTON_1;
                
            case MouseEvent.BUTTON2:
                return MOUSE_BUTTON_2;
                
            case MouseEvent.BUTTON3:
                return MOUSE_BUTTON_3;
                
            default:
                return -1;
        }
    }

    
    /**
     * Retorna a GameAction associada ao MouseEvent.
     */
    private GameAction getMouseButtonAction( MouseEvent e ) {
        int mouseCode = getMouseButtonCode( e );
        if ( mouseCode != -1 ) {
             return mouseActions[ mouseCode ];
        } else {
             return null;
        }
    }

    
    public void keyTyped( KeyEvent e ) {
        // d� certeza que a tecla n�o � processada por mais ningu�m
        e.consume();
    }

    
    public void keyPressed( KeyEvent e ) {
        GameAction gameAction = getKeyAction( e );
        if ( gameAction != null ) {
            gameAction.press();
        }
        // d� certeza que a tecla n�o � processada por mais ningu�m
        e.consume();
    }

    
    public void keyReleased( KeyEvent e ) {
        GameAction gameAction = getKeyAction( e );
        if ( gameAction != null ) {
            gameAction.release();
        }
        // d� certeza que a tecla n�o � processada por mais ningu�m
        e.consume();
    }

    
    public void mouseClicked( MouseEvent e ) {
        // n�o faz nada
    }

    
    public void mousePressed( MouseEvent e ) {
        GameAction gameAction = getMouseButtonAction( e );
        if ( gameAction != null ) {
            gameAction.press();
        }
    }

    
    public void mouseReleased( MouseEvent e ) {
        GameAction gameAction = getMouseButtonAction( e );
        if ( gameAction != null ) {
            gameAction.release();
        }
    }

    public void mouseEntered(MouseEvent e) {
        mouseMoved( e );
    }

    
    public void mouseExited(MouseEvent e) {
        mouseMoved( e );
    }

    
    public void mouseDragged(MouseEvent e) {
        mouseMoved( e );
    }

    
    public synchronized void mouseMoved(MouseEvent e) {
        // este evento � para recentralizar o mouse
        if ( isRecentering &&
            centerLocation.x == e.getX() &&
            centerLocation.y == e.getY() ) {
            isRecentering = false;
        } else {
            int dx = e.getX() - mouseLocation.x;
            int dy = e.getY() - mouseLocation.y;
            mouseHelper( MOUSE_MOVE_LEFT, MOUSE_MOVE_RIGHT, dx );
            mouseHelper( MOUSE_MOVE_UP, MOUSE_MOVE_DOWN, dy );
            if ( isRelativeMouseMode() ) {
                recenterMouse();
            }
        }

        mouseLocation.x = e.getX();
        mouseLocation.y = e.getY();
    }

    
    public void mouseWheelMoved(MouseWheelEvent e) {
        mouseHelper( MOUSE_WHEEL_UP, MOUSE_WHEEL_DOWN, e.getWheelRotation() );
    }
    
    /**
     * Calcula e configura a movimenta��o do mouse.
     */
    private void mouseHelper( int codeNeg, int codePos, int amount ) {
        GameAction gameAction;
        if ( amount < 0 ) {
            gameAction = mouseActions[ codeNeg ];
        } else {
            gameAction = mouseActions[ codePos ];
        }
        
        if ( gameAction != null ) {
            gameAction.press( Math.abs(amount) );
            gameAction.release();
        }
    }

}