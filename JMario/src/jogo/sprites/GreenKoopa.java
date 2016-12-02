package jogo.sprites;

import infraestrutura.grafico.Animation;

/**
 * Um GreenKoopa � uma Creature que se move devagar no ch�o.
 */
public class GreenKoopa extends Creature {
    
    public GreenKoopa( Animation left, Animation right,
            Animation deadLeft, Animation deadRight ) {
        super( left, right, deadLeft, deadRight );
        setMaxSpeed( 0.1f );
    }
    
}