package tit07.morris.controller.ai;

import tit07.morris.model.ModelAIInterface;
import tit07.morris.model.State;
import tit07.morris.model.StoneColor;


/**
 * KI-Implementierung des Minimax-Algorithmus mit AlphaBeta-Suche.
 */
public class AIMinimax implements Calculateable {

    /** Referenz auf die einzige Instanz der Klasse AIMinimax */
    private static AIMinimax instance = new AIMinimax();

    /** Heuristik, welche als Entscheidungsgrundlage für Spielsituationen dient */
    private Decideable       heuristic;

    /** Maximierender Spieler, welcher am Zug ist */
    private StoneColor       maxPlayer;

    /** Minimierender Spieler, welcher nicht am Zug ist */
    private StoneColor       minPlayer;

    /** Suchtiefe bis zu welcher der Spielbaums rekursiv untersucht wird */
    private int              initDepth;

    /** Dynamische Suchtiefe */
    private int              dynamicDepth;

    /** Variable für den Index des besten gefundenen Spielzuges */
    private int              indexOfBestMove;


    /**
     * Privater Konstruktor, da keine weiteren Instanzen erzeugt werden sollen
     */
    private AIMinimax() {

    }

    /**
     * Gibt ein Handle mit der Minimax-Implementierung zurück
     * 
     * @param heuristic Implementierung einer Heuristik für einen Spielstand
     * @param depth Suchtiefe, bis zu welcher der Spielbaum durchsucht werden
     *            soll (mind. 1)
     * @return Instanz der Minimax-Implementierung oder null bei ungültigen
     *         Parametern
     */
    public static AIMinimax getInstance( Decideable heuristic, int depth ) {

        if( heuristic instanceof Decideable && depth >= 1 ) {
            instance.heuristic = heuristic;
            instance.initDepth = depth;
            return instance;
        }
        return null;
    }

    /**
     * Implementierung der Spielzugberechnung durch den Minimax-Algorithmus
     * 
     * @param gameHandle Referenz auf das aktuelle Spiel
     * @return Berechneter Spielzug der KI
     */
    @Override
    public AIMove calculateNextMove( ModelAIInterface gameHandle ) {

        /* Generiere alle möglichen Spielzüge */
        AIPossibleMoves possibleMoves = new AIPossibleMoves( gameHandle );

        /* Setze die Zugdaten des ausgewähltes Zuges */
        AIMove chosenMove = new AIMove();
        chosenMove.setActiveColor( possibleMoves.getActiveColor() );
        chosenMove.setAction( possibleMoves.getAction() );

        /* Setze den aktiven und passiven Spieler */
        if( chosenMove.getActiveColor() == StoneColor.BLACK ) {
            this.maxPlayer = StoneColor.BLACK;
            this.minPlayer = StoneColor.WHITE;
        }
        else if( chosenMove.getActiveColor() == StoneColor.WHITE ) {
            this.maxPlayer = StoneColor.WHITE;
            this.minPlayer = StoneColor.BLACK;
        }

        /* Starte den Minimax-Algorithmus */
        this.dynamicDepth = initDepth;
        if( chosenMove.getAction() == State.JUMP && initDepth >= 5 ) {
            this.dynamicDepth--;
        }
        this.calculateMaxValue( this.dynamicDepth,
                                gameHandle,
                                Integer.MIN_VALUE,
                                Integer.MAX_VALUE );

        /* Garbarge-Collector aufgerufen, nach Zugberechnung */
        System.gc();

        /* Setzt das gefundene Ergebnis */
        chosenMove.setTo( possibleMoves.getTo().get( indexOfBestMove ) );
        if( possibleMoves.getAction() == State.MOVE
                || possibleMoves.getAction() == State.JUMP ) {

            chosenMove.setFrom( possibleMoves.getFrom().get( indexOfBestMove ) );
        }
        return chosenMove;
    }

    /**
     * Berechnet für den maximierenden Spieler den bestmöglichen Zug
     * 
     * @param depth Verbleibende Suchtiefe im Spielbaum
     * @param gameHandle Referenz auf den zum Zeitpunkt simulierten Spielstand
     * @param alpha Untergrenze des bestmöglichen Zuges
     * @param beta Obergrenze des bestmöglichen Zuges
     * @return Wert des bestmöglichen Zuges (je positiver desto besser)
     */
    private int calculateMaxValue( int depth,
                                   ModelAIInterface gameHandle,
                                   int alpha,
                                   int beta ) {

        /* Alle möglichen Spielzüge generieren */
        AIPossibleMoves possibleMoves = new AIPossibleMoves( gameHandle );

        /*
         * Abbruchbedingung: Rekursionstiefe erreicht oder keine weiteren Züge
         * möglich
         */
        if( depth <= 0 || possibleMoves.getNumberOfMoves() <= 0 ) {
            return getEvaluation( gameHandle );
        }

        /* Alle möglichen Spielzüge simulieren */
        for( int i = 0; i < possibleMoves.getNumberOfMoves(); i++ ) {

            ModelAIInterface gameClone = gameHandle.clone();

            try {
                /* Entfernen-Spielzug ausführen */
                if( depth == dynamicDepth
                        && ( gameClone.getState( StoneColor.WHITE ) == State.REMOVE || gameClone
                                                                                                .getState( StoneColor.BLACK ) == State.REMOVE ) ) {

                    // System.out.println(
                    // "BestRemoveIndex: "+indexOfBestRemove);
                    this.indexOfBestMove = calculateBestRemove( gameClone );
                    return 0;
                }

                /* Reguläre Spielzüge simulieren */
                switch( possibleMoves.getAction() ) {
                case SET:
                    gameClone.setStone( possibleMoves.getTo().get( i ) );
                    break;
                case MOVE:
                case JUMP:
                    gameClone.moveStone( possibleMoves.getFrom().get( i ),
                                         possibleMoves.getTo().get( i ) );
                    break;
                }

                /* Simuliere das entfernen eines Spielsteins simulieren */
                if( gameClone.getState( StoneColor.WHITE ) == State.REMOVE
                        || gameClone.getState( StoneColor.BLACK ) == State.REMOVE ) {
                    gameClone = simulateBestRemove( gameClone );
                }

                /* Spielzug, der zum Sieg führt direkt ausführen */
                if( depth == dynamicDepth
                        && gameClone.getState( maxPlayer ) == State.WINNER ) {
                    this.indexOfBestMove = i;
                    return 0;
                }
            }
            catch( Exception e ) {
            }

            /* Nächsten Spielzug berechnen */
            int calculatedValue = calculateMinValue( depth - 1,
                                                     gameClone,
                                                     alpha,
                                                     beta );

            /* Beta-Cut */
            if( calculatedValue >= beta ) {
                return beta;
            }
            /* Besserer Zug gefunden */
            if( calculatedValue > alpha ) {
                alpha = calculatedValue;
                if( depth == dynamicDepth ) {
                    this.indexOfBestMove = i;
                }
            }
            /* Zufällige Auswahl bei gleichwertigen Zügen */
            // else if(depth == initDepth && calculatedValue == alpha) {
            // indexOfBestMove = ( new java.util.Random().nextBoolean() ) ? i :
            // indexOfBestMove;
            // }
        }
        return alpha;
    }

    /**
     * Berechnet für den minimierenden Spieler den bestmöglichen Folge-Zug
     * 
     * @param depth Verbleibende Suchtiefe im Spielbaum
     * @param gameHandle Referenz auf den zum Zeitpunkt simulierten Spielstand
     * @param alpha Untergrenze des bestmöglichen Zuges
     * @param beta Obergrenze des bestmöglichen Zuges
     * @return Wert des bestmöglichen Zuges (je negativer desto besser)
     */
    private int calculateMinValue( int depth,
                                   ModelAIInterface gameHandle,
                                   int alpha,
                                   int beta ) {

        /* Alle möglichen Spielzüge generieren */
        AIPossibleMoves possibleMoves = new AIPossibleMoves( gameHandle );

        /*
         * Abbruchbedingung: Rekursionstiefe erreicht oder keine Spielzüge mehr
         * möglich
         */
        if( depth <= 0 || possibleMoves.getNumberOfMoves() == 0 ) {
            return getEvaluation( gameHandle );
        }

        /* Alle möglichen Spielzüge simulieren */
        for( int i = 0; i < possibleMoves.getNumberOfMoves(); i++ ) {

            ModelAIInterface gameClone = gameHandle.clone();
            try {
                switch( possibleMoves.getAction() ) {
                case SET:
                    gameClone.setStone( possibleMoves.getTo().get( i ) );
                    break;
                case MOVE:
                case JUMP:
                    gameClone.moveStone( possibleMoves.getFrom().get( i ),
                                         possibleMoves.getTo().get( i ) );
                    break;
                }
                /* Simuliere das entfernen eines Spielsteins */
                if( gameClone.getState( StoneColor.WHITE ) == State.REMOVE
                        || gameClone.getState( StoneColor.BLACK ) == State.REMOVE ) {

                    gameClone = simulateBestRemove( gameClone );
                }

                /*
                 * Spielzug, der zur direkten Niederlage führen kann möglichst
                 * verhindern
                 */
                // if( depth == dynamicDepth-1 && gameClone.getState( minPlayer
                // ) == State.WINNER ) {
                // return Integer.MIN_VALUE-1;
                // }
            }
            catch( Exception e ) {
            }

            /* Nächsten Spielzug berücksichtigen */
            int calculatedValue = calculateMaxValue( depth - 1,
                                                     gameClone,
                                                     alpha,
                                                     beta );

            /* Alpha-Cut */
            if( calculatedValue <= alpha ) {
                return alpha;
            }
            /* Besserer Zug gefunden */
            if( calculatedValue < beta ) {
                beta = calculatedValue;
            }
        }
        return beta;
    }

    /**
     * Berechnet welcher entfernte Stein am meisten Vorteile bringt.
     * 
     * @param game Referenz auf das simulierte Spiel, wo ein Stein entfernt
     *            werden soll
     * @return Index des besten Zuges
     * @throws Exception Wird geworfen, wenn kein Stein entfernt werden muss.
     */
    private int calculateBestRemove( ModelAIInterface game ) throws Exception {

        /* Generiere alle möglichen Spielzüge */
        AIPossibleMoves moves = new AIPossibleMoves( game );
        try {
            if( game.getState( StoneColor.BLACK ) != State.REMOVE
                    && game.getState( StoneColor.WHITE ) != State.REMOVE ) {

                throw new Exception( Messages.getString("AIMinimax.no_stone_must_be_removed") ); //$NON-NLS-1$
            }

            int indexOfBestRemove = 0;
            int bestFoundValue = Integer.MIN_VALUE;
            int evaluationFactor = ( moves.getActiveColor() == StoneColor.WHITE ) ? -1
                                                                                 : 1;

            /* Simuliere alle möglichen Spielzüge */
            for( int i = 0; i < moves.getNumberOfMoves(); i++ ) {

                ModelAIInterface gameClone = game.clone();
                gameClone.removeStone( moves.getTo().get( i ) );

                /* Untersuche gegnerische Möglichkeiten */
                AIPossibleMoves possibleMoves = new AIPossibleMoves( gameClone );
                int minValue = Integer.MAX_VALUE;
                for( int j = 0; j < possibleMoves.getNumberOfMoves(); j++ ) {

                    /*
                     * Simuliere nächsten Zug des Gegners (minimierender
                     * Spieler)
                     */
                    ModelAIInterface minClone = gameClone.clone();
                    switch( possibleMoves.getAction() ) {
                    case SET:
                        minClone.setStone( possibleMoves.getTo().get( j ) );
                        break;
                    case MOVE:
                    case JUMP:
                        minClone.moveStone( possibleMoves.getFrom().get( j ),
                                            possibleMoves.getTo().get( j ) );
                        break;
                    }
                    // int calcValue =
                    // heuristic.evaluateBorder(gameClone)*evaluationFactor;
                    int calcValue = HeuristicRemove.getInstance()
                                                   .evaluateBorder( minClone )
                            * evaluationFactor;
                    if( calcValue < minValue ) {
                        minValue = calcValue;
                    }
                }

                /* Neuen besten Spielzug gefunden */
                if( minValue >= bestFoundValue ) {
                    bestFoundValue = minValue;
                    indexOfBestRemove = i;
                }
            }
            return indexOfBestRemove;
        }
        catch( Exception e ) {
            throw new Exception( e.getMessage() );
        }
    }

    /**
     * Simuliert das entfernen eines Steins vom Spielbrett. Dabei wird der Stein
     * entfernt, welcher anhand der Heuristik die meisten Vorteile bringt.
     * 
     * @param game Referenz auf das simulierte Spiel, wo ein Stein entfernt
     *            werden soll
     * @return Referenz auf das simulierte Spiel, nachdem der Stein entfernt
     *         wurde
     * @throws Exception Wird geworfen, wenn kein Stein entfernt werden muss.
     */
    private ModelAIInterface simulateBestRemove( ModelAIInterface game )
                                                                        throws Exception {

        /* Generiere alle möglichen Spielzüge */
        AIPossibleMoves moves = new AIPossibleMoves( game );
        try {
            if( game.getState( StoneColor.BLACK ) != State.REMOVE
                    && game.getState( StoneColor.WHITE ) != State.REMOVE ) {

                throw new Exception( Messages.getString("AIMinimax.no_stone_must_be_removed") ); //$NON-NLS-1$
            }

            int indexOfBestRemove = 0;
            int bestFoundValue = Integer.MIN_VALUE;
            int evaluationFactor = ( moves.getActiveColor() == StoneColor.WHITE ) ? -1
                                                                                 : 1;

            /* Simuliere alle möglichen Spielzüge */
            for( int i = 0; i < moves.getNumberOfMoves(); i++ ) {

                ModelAIInterface gameClone = game.clone();
                gameClone.removeStone( moves.getTo().get( i ) );

                int calcValue = HeuristicRemove.getInstance()
                                               .evaluateBorder( gameClone )
                        * evaluationFactor;

                /* Neuen besten Spielzug gefunden */
                if( calcValue >= bestFoundValue ) {
                    bestFoundValue = calcValue;
                    indexOfBestRemove = i;
                }
            }

            /* Führe den besten gefundenen Zug aus */
            ModelAIInterface changedGame = game.clone();
            changedGame.removeStone( moves.getTo().get( indexOfBestRemove ) );
            if( changedGame.getState( StoneColor.BLACK ) == State.REMOVE
                    || changedGame.getState( StoneColor.WHITE ) == State.REMOVE ) {

                throw new Exception( Messages.getString("AIMinimax.remove_not_suc") ); //$NON-NLS-1$
            }
            return changedGame;
        }
        catch( Exception e ) {
            throw new Exception( e.getMessage() );
        }
    }

    /**
     * Gibt bei einer Evaluierung des Spielfeldes den positiven Wert für den
     * maximierenden und den negativen Wert für den minimierenden Spieler
     * zurück.
     * 
     * @param game Referenz auf die aktuelle Spielsimulation
     * @return Evaluierung des Spielfeldes
     */
    private int getEvaluation( ModelAIInterface game ) {

        /*
         * Wert der Heuristik: positiv ist gut für schwarz, negativ ist gut für
         * weiß
         */
        int value = heuristic.evaluateBorder( game );

        try {
            /* Invertiere den Wert, wenn weiß der maximierende Spieler ist */
            if( maxPlayer == StoneColor.WHITE && minPlayer == StoneColor.BLACK ) {
                value *= -1;
            }
        }
        catch( Exception e ) {
        }
        return value;
    }
}
