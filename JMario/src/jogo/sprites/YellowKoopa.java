package jogo.sprites;

import infraestrutura.grafico.Animation;

/**
 * Um YellowKoopa � uma Creature que se move devagar no ch�o.
 */
public class YellowKoopa extends Creature {
    
    public YellowKoopa( Animation left, Animation right,
            Animation deadLeft, Animation deadRight ) {
        super( left, right, deadLeft, deadRight );
        setMaxSpeed( 0.1f );
    }
    
}