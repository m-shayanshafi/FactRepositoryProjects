package jogo.sprites;

import infraestrutura.grafico.Animation;

/**
 * Um Turtle � uma Creature que se move devagar no ch�o.
 */
public class Turtle extends Creature {
    
    public Turtle( Animation left, Animation right,
            Animation deadLeft, Animation deadRight ) {
        super( left, right, deadLeft, deadRight );
        setMaxSpeed( 0.1f );
    }
    
}