package jogo.sprites;

import infraestrutura.grafico.Animation;

/**
 * Um Goomba � uma Creature que se move devagar no ch�o.
 */
public class Goomba extends Creature {
    
    public Goomba( Animation left, Animation right,
            Animation deadLeft, Animation deadRight ) {
        super( left, right, deadLeft, deadRight );
        setMaxSpeed( 0.1f );
    }
    
}