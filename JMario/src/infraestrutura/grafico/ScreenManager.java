package infraestrutura.grafico;

import java.awt.*;
import java.awt.image.*;
import java.lang.reflect.*;
import javax.swing.*;

/**
 * A classe ScreenManager gerencia a inicializa��o e visualiza��o de
 * modos de tela cheia.
 *
 * @author David Buzatto
 */
public class ScreenManager {
    
    private GraphicsDevice device;
    
    
    /**
     * Cria um novo ScreenManager.
     */
    public ScreenManager() {
        
        GraphicsEnvironment environment =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = environment.getDefaultScreenDevice();
        
    }
    
    
    /**
     * Retorna uma lista de modos de visualiza��o compat�veis com o dispositivo
     * padr�o no sistema.
     */
    public DisplayMode[] getCompatibleDisplayModes() {
        
        return device.getDisplayModes();
        
    }
    
    
    /**
     * Retorna o primeiro modo compat�vel da lista de modos.
     * Retorna null se nenhum modo � compat�vel.
     */
    public DisplayMode findFirstCompatibleMode( DisplayMode[] modes ) {
        
        DisplayMode[] goodModes = device.getDisplayModes();
        
        for ( int i = 0; i < modes.length; i++ ) {
            for ( int j = 0; j < goodModes.length; j++ ) {
                if ( displayModesMatch( modes[ i ], goodModes[ j ] ) ) {
                    return modes[ i ];
                }
            }
        }
        
        return null;
        
    }
    
    
    /**
     * Retorna o modo de visualiza��o atual.
     */
    public DisplayMode getCurrentDisplayMode() {
        return device.getDisplayMode();
    }
    
    
    /**
     * Determina se dois modos de visualiza��o s�o iguais.
     * Dois modos de visualiza��o s�o iguais se eles t�m a mesma resolu��o,
     * profundidade de bits e taxa de atualiza��o. A profundidade de bits �
     * ignorada se um dos modos tem uma profundidade de DisplayMode.BIT_DEPTH_MULTI.
     * Da mesma forma, a taxa de atualiza��o � ignorada se um dos modos de
     * visualiza��o tiver uma taxa de DisplayMode.REFRESH_RATE_UNKNOWN.
     */
    public boolean displayModesMatch( DisplayMode mode1, DisplayMode mode2 ) {
        
        if ( mode1.getWidth() != mode2.getWidth() ||
                mode1.getHeight() != mode2.getHeight() ) {
            return false;
        }
        
        if ( mode1.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI &&
                mode2.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI &&
                mode1.getBitDepth() != mode2.getBitDepth() ) {
            return false;
        }
        
        if ( mode1.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN &&
                mode2.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN &&
                mode1.getRefreshRate() != mode2.getRefreshRate() ) {
            return false;
        }
        
        return true;
        
    }
    
    
    /**
     * Entra no modo de tela cheia o muda o modo de visualiza��o.
     * Se o modo de visualiza��o especificado � null ou n�o compat�vel com
     * este dispositivo, os o modo de visualiza��o n�o puder ser alterado nesse
     * sistema, o modo de visualiza��o atual � utilizado.
     * <p>
     * A visualiza��o usa um BufferStrategy com 2 buffers.
     */
    public void setFullScreen( DisplayMode displayMode ) {
        
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setUndecorated( true );
        frame.setIgnoreRepaint( true );
        frame.setResizable( false );
        
        device.setFullScreenWindow(frame);
        
        if (displayMode != null &&
                device.isDisplayChangeSupported()) {
            try {
                device.setDisplayMode(displayMode);
            } catch (IllegalArgumentException ex) { }
            // fix para o Mac OS
            frame.setSize(displayMode.getWidth(),
                    displayMode.getHeight());
        }
        
        // evita deadlock no Java 1.4
        try {
            EventQueue.invokeAndWait(
                    new Runnable() {
                        public void run() {
                            frame.createBufferStrategy( 2 );
                        }
                    }
            );
        } catch ( InterruptedException ex ) {
            // ignora
        } catch ( InvocationTargetException  ex ) {
            // ignora
        }
        
    }
    
    
    /**
     * Obt�m o contexto gr�fico para a visualiza��o. O ScreenManager
     * usa double buffering, ent�o as aplica��es devem chamar update()
     * para mostrar qualquer desenho que foi desenhado.
     * <p>
     * A aplica��o deve dispor do objeto gr�fico.
     */
    public Graphics2D getGraphics() {
        
        Window window = device.getFullScreenWindow();
        
        if ( window != null ) {
            
            BufferStrategy strategy = window.getBufferStrategy();
            return ( Graphics2D ) strategy.getDrawGraphics();
            
        } else {
            
            return null;
            
        }
        
    }
    
    
    /**
     * Atualiza a visualiza��o.
     */
    public void update() {
        
        Window window = device.getFullScreenWindow();
        
        if ( window != null ) {
            
            BufferStrategy strategy = window.getBufferStrategy();
            if ( !strategy.contentsLost() ) {
                strategy.show();
            }
            
        }
        
        // sincroniza a visualiza��o em alguns sistemas
        Toolkit.getDefaultToolkit().sync();
        
    }
    
    
    /**
     * Retorna a janela utilizada no modo de tela cheia atual.
     * Retorna null se o dispositivo n�o est� no modo de tela cheia.
     */
    public Window getFullScreenWindow() {
        
        return device.getFullScreenWindow();
        
    }
    
    
    /**
     * Retorna a largura da janela usada atualmente no modo de tela cheia.
     * Retorna zero se o dispositivo n�o estiver no modo de tela cheia.
     */
    public int getWidth() {
        
        Window window = device.getFullScreenWindow();
        
        if ( window != null ) {
            
            return window.getWidth();
            
        } else {
            
            return 0;
            
        }
        
    }
    
    
    /**
     * Retorna a altura da janela usada atualmente no modo de tela cheia.
     * Retorna zero se o dispositivo n�o estiver no modo de tela cheia.
     */
    public int getHeight() {
        
        Window window = device.getFullScreenWindow();
        
        if ( window != null ) {
            
            return window.getHeight();
            
        } else {
            
            return 0;
            
        }
        
    }
    
    
    /**
     * Restaura o modo de visualiza��o da tela.
     */
    public void restoreScreen() {
        
        Window window = device.getFullScreenWindow();
        
        if ( window != null  ) {
            window.dispose();
        }
        
        device.setFullScreenWindow( null );
        
    }
    
    
    /**
     * Cria uma imagem compat�vel com a visualiza��o atual.
     */
    public BufferedImage createCompatibleImage( int w, int h, int transparency ) {
        
        Window window = device.getFullScreenWindow();
        
        if ( window != null ) {
            
            GraphicsConfiguration gc = window.getGraphicsConfiguration();
            return gc.createCompatibleImage( w, h, transparency );
            
        }
        
        return null;
        
    }
    
}