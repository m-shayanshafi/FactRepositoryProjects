package jogo.sprites;

import infraestrutura.grafico.Animation;
import infraestrutura.grafico.Sprite;
import java.lang.reflect.Constructor;

/**
 * PowerUp � uma Sprite que o jogador pode pegar.
 */
public abstract class PowerUp extends Sprite {
    
    public PowerUp( Animation anim ) {
        super( anim );
    }
    
    
    public Object clone() {
        // usa reflex�o para criar a subclasse correta
        Constructor constructor = getClass().getConstructors()[ 0 ];
        try {
            return constructor.newInstance(
                    new Object[] { ( Animation)anim.clone() } );
        } catch ( Exception ex ) {
            // provavelmente nunca ir� acontecer
            ex.printStackTrace();
            return null;
        }
    }
    
    
    /**
     * O PowerUp Coin. D� pontos ao jogador.
     */
    public static class Coin extends PowerUp {
        public Coin( Animation anim ) {
            super( anim );
        }
    }
    
    
    /**
     * O PowerUp Mushroom faz o jogador crescer.
     */
    public static class Mushroom extends PowerUp {
        public Mushroom( Animation anim ) {
            super( anim );
        }
    }
    
    /**
     * O PowerUp OneUpe d� uma vida ao jogador.
     */
    public static class OneUp extends PowerUp {
        public OneUp( Animation anim ) {
            super( anim );
        }
    }
    
    /**
     * O PowerUp FireFlower d� ao jogador poder de jogar fogo.
     */
    public static class FireFlower extends PowerUp {
        public FireFlower( Animation anim ) {
            super( anim );
        }
    }
    
    
    /**
     * O PowerUp Goal. Avan�a para o pr�ximo mapa.
     */
    public static class Goal extends PowerUp {
        public Goal( Animation anim ) {
            super( anim );
        }
    }
    
}