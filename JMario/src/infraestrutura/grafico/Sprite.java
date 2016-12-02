package infraestrutura.grafico;

import java.awt.*;

/**
 * A classe Sprite define uma entidade do jogo, algo que recebe anima��es.
 *
 * @author David Buzatto
 */
public class Sprite {
    
    protected Animation anim;
    
    // posicionamento
    private float x;
    private float y;
    
    // velocidade (pixels por milisegundo)
    private float velocityX;
    private float velocityY;
    
    
    /*
     * Cria um novo objeto Sprite com a anima��o especificada.
     */
    public Sprite( Animation anim ) {
        this.anim = anim;
    }
    
    
    /**
     * Atualiza a Anima��o da Sprite e sua posi��o baseada na velocidade.
     */
    public void update( long elapsedTime ) {
        x += velocityX * elapsedTime;
        y += velocityY * elapsedTime;
        anim.update( elapsedTime );
    }
    
    
    /**
     * Obt�m a posi��o x atual da sprite.
     */
    public float getX() {
        return x;
    }
    
    
    /**
     * Configura a posi��o x atual da sprite.
     */
    public void setX(float x) {
        this.x = x;
    }
    
    
    /**
     * Obt�m a posi��o y atual da sprite.
     */
    public float getY() {
        return y;
    }
    
    
    /**
     * Configura a posi��o x atual da sprite.
     */
    public void setY(float y) {
        this.y = y;
    }
    
    
    /**
     * Obt�m a velocidade horizontal da sprite em pixels por milisegundo.
     */
    public float getVelocityX() {
        return velocityX;
    }
    
    
    /**
     * Configura a velocidade horizontal da sprite em pixels por milisegundo.
     */
    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }
    
    
    /**
     * Obt�m a velocidade vertical da sprite em pixels por milisegundo.
     */
    public float getVelocityY() {
        return velocityY;
    }
    
    
    /**
     * Configura a velocidade vertical da sprite em pixels por milisegundo.
     */
    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }
    
    
    /**
     * Obt�m a largura da Sprite, baseado no tamanho da imagem atual.
     */
    public int getWidth() {
        return anim.getImage().getWidth( null );
    }
    
    
    /**
     * Obt�m a altura da Sprite, baseado no tamanho da imagem atual.
     */
    public int getHeight() {
        return anim.getImage().getHeight( null );
    }
    
    
    /**
     * Obt�m a imagem atual da Sprite.
     */
    public Image getImage() {
        return anim.getImage();
    }
    
    
    /**
     * Clona a Sprite. N�o clona a posi��o ou valocidade.
     */
    public Object clone() {
        return new Sprite( anim );
    }
    
}