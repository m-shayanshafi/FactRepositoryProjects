package jogo.sprites;

import infraestrutura.grafico.Animation;

/**
 * Um RedKoopa � uma Creature que se move devagar no ch�o.
 */
public class RedKoopa extends Creature {
    
    public RedKoopa( Animation left, Animation right,
            Animation deadLeft, Animation deadRight ) {
        super( left, right, deadLeft, deadRight );
        setMaxSpeed( 0.1f );
    }
    
}