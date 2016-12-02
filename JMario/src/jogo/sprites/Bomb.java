package jogo.sprites;

import infraestrutura.grafico.Animation;

/**
 * Um Bomb � uma Creature que se move devagar no ch�o.
 */
public class Bomb extends Creature {
    
    public Bomb( Animation left, Animation right,
            Animation deadLeft, Animation deadRight ) {
        super( left, right, deadLeft, deadRight );
        setMaxSpeed( 0.1f );
    }
    
}