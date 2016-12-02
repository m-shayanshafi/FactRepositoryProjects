package jogo.tile;

import infraestrutura.grafico.*;
import java.awt.Image;
import java.io.*;
import java.util.*;

/**
 * A classe TileMap cont�m a informa��o para um pequeno mapa de figuras
 * lado a lado, incluindo Sprites.
 * Cada peda�o � uma refer�ncia a uma imagem, sendo essas imagens usadas
 * m�ltiplas vezes no mesmo mapa.
 *
 * @author David Buzatto
 */
public class TileMap {
    
    private Image[][] tiles;
    private LinkedList< Sprite > sprites;
    private Sprite player;
    
    /**
     * Cria um novo TileMap com a largura e altura especificada
     * (em n�mero de peda�os) do mapa.
     */
    public TileMap( int width, int height ) {
        tiles = new Image[ width ][ height ];
        sprites = new LinkedList< Sprite >();
    }
    
    
    /**
     * Obt�m a largura do TileMap (n�mero de peda�os).
     */
    public int getWidth() {
        return tiles.length;
    }
    
    /**
     * Obt�m a altura do TileMap (n�mero de peda�os).
     */
    public int getHeight() {
        return tiles[ 0 ].length;
    }
    
    
    /**
     * Obt�m o peda�o de uma localiza��o espef�fica. Retorna null is
     * n�o haver nenhum peda�o na localiza��o espeficada ou ent�o se a
     * localiza��ono for fora dos limites do mapa.
     */
    public Image getTile( int x, int y ) {
        if ( x < 0 || x >= getWidth() ||
                y < 0 || y >= getHeight() ) {
            return null;
        } else {
            return tiles[ x ][ y ];
        }
    }
    
    
    /**
     * Configura o peda�o no local especificado.
     */
    public void setTile( int x, int y, Image tile ) {
        tiles[ x ][ y ] = tile;
    }
    
    
    /**
     * Obt�m a Sprite do jogador.
     */
    public Sprite getPlayer() {
        return player;
    }
    
    
    /**
     * Configura a Sprite do jogador.
     */
    public void setPlayer( Sprite player ) {
        this.player = player;
    }
    
    
    /**
     * Adiciona a Sprite no mapa.
     */
    public void addSprite( Sprite sprite ) {
        sprites.add( sprite );
    }
    
    
    /**
     * Remove a Sprite do mapa.
     */
    public void removeSprite( Sprite sprite ) {
        sprites.remove( sprite );
    }
    
    
    /**
     * Obt�m o Iterator de todas as Sprites desse mapa, menos a do jogador.
     */
    public Iterator getSprites() {
        return sprites.iterator();
    }
    
}