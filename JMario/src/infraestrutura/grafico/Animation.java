package infraestrutura.grafico;

import java.awt.*;
import java.util.*;

/**
 * A classe Animation gerencia uma s�rie de imagens (quadros) e a quantidade
 * de tempo para exibir cada imagem.
 *
 * @author David Buzatto
 */
public class Animation {
    
    private ArrayList< AnimFrame > frames;
    private int currFrameIndex;
    private long animTime;
    private long totalDuration;
    
    
    /**
     * Cria uma nova Animation vazia.
     */
    public Animation() {
        this( new ArrayList< AnimFrame >(), 0 );
    }
    
    
    /**
     * Cria uma anima��o com os frames e a dura��o total da exibi��o 
     * da anima��o.
     */
    private Animation( ArrayList< AnimFrame > frames, long totalDuration ) {
        this.frames = frames;
        this.totalDuration = totalDuration;
        start();
    }
    
    
    /**
     * Cria uma duplicata da anima��o. A lista de frames � compartilhada 
     * entre duas anima��es, mas cada anima��o pode ser animada 
     * independentemente.
     */
    public Object clone() {
        return new Animation( frames, totalDuration );
    }
    
    
    /**
     * Adiciona uma imagem � anima��o com uma dura��o especificada.
     * (tempo para exibir a imagem).
     */
    public synchronized void addFrame( Image image, long duration ) {
        
        totalDuration += duration;
        frames.add( new AnimFrame( image, totalDuration ) );
        
    }
    
    
    /**
     * Inicia a anima��o desde o in�cio.
     */
    public synchronized void start() {
        animTime = 0;
        currFrameIndex = 0;
    }
    
    
    /**
     * Atualiza o quadro atual desta anima��o, se necess�rio.
     */
    public synchronized void update( long elapsedTime ) {
        
        if ( frames.size() > 1 ) {
            
            animTime += elapsedTime;
            
            if ( animTime >= totalDuration ) {
                
                animTime = animTime % totalDuration;
                currFrameIndex = 0;
                
            }
            
            while ( animTime > getFrame( currFrameIndex ).endTime ) {
                
                currFrameIndex++;
                
            }
            
        }
        
    }
    
    
    /**
     * Obt�m a imagem atual da anima��o. Retorna null se a anima��o
     * n�o tiver nenhuma imagem.
     */
    public synchronized Image getImage() {
        
        if ( frames.size() == 0 ) {
            
            return null;
            
        } else {
            
            return getFrame( currFrameIndex ).image;
            
        }
        
    }
    
    /**
     * Obt�m um frame da anima��o.
     */
    private AnimFrame getFrame( int i ) {
        
        return ( AnimFrame ) frames.get( i );
        
    }
    
    
    /**
     * Class interna privada que modela um frame (quadro) da anima��o.
     */
    private class AnimFrame {
        
        Image image;
        long endTime;
        
        /**
         * Cria uma novo AnimFrame com uma imagem e o tempo de dura��o.
         */
        public AnimFrame( Image image, long endTime ) {
            
            this.image = image;
            this.endTime = endTime;
            
        }
        
    }
    
}