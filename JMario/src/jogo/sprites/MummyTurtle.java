package jogo.sprites;

import infraestrutura.grafico.Animation;

/**
 * Um MummyTurtle � uma Creature que se move devagar no ch�o.
 */
public class MummyTurtle extends Creature {
    
    public MummyTurtle( Animation left, Animation right,
            Animation deadLeft, Animation deadRight ) {
        super( left, right, deadLeft, deadRight );
        setMaxSpeed( 0.1f );
    }
    
}