package jogo.sprites;

import infraestrutura.grafico.Animation;

/**
 * Um BlueDragon � uma Creature que se move devagar no ch�o.
 */
public class BlueDragon extends Creature {
    
    public BlueDragon( Animation left, Animation right,
            Animation deadLeft, Animation deadRight ) {
        super( left, right, deadLeft, deadRight );
        setMaxSpeed( 0.1f );
    }
    
}