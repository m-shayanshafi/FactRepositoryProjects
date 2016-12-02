package infraestrutura.som;

/**
 * Classe abstrata desenvolvida para filtrar amostras de som.
 * Como os SoundFilters podem usar buferiza��o interna de amostras,
 * um novo SoundFilter deve ser criado para cada som executado.
 * Entretanto, SoundFilters podem ser reusados ap�s serem finalizados chamando o 
 * m�todo reset().
 * Assume-se que todas as amostrars s�o de 16-bit, sinalizadas, e no formato 
 * "little-endian".
 * @see FilteredSoundStream
 *
 * @author David Buzatto
 */
public abstract class SoundFilter {
    
    /**
     * Reseta esse SoundFilter. N�o faz nada por padr�o.
     */
    public void reset() {
        // n�o faz nada
    }
    
    
    /**
     * Obt�m o tamanho restante, em bytes, que esre filtro executa ap�s o som 
     * ser finalizado.that this filter. Um exemplo pode ser um �co que executa 
     * mais que o seu som original.
     * Esse m�todo retorna 0 por padr�o.
     */
    public int getRemainingSize() {
        return 0;
    }
    
    
    /**
     * Filtra um array de amostrar. As amostras devem ser de 16-bit, 
     * sinalizadas e no formato "little-endian".
     */
    public void filter( byte[] samples ) {
        filter( samples, 0, samples.length );
    }
    
    
    /**
     * Filtra um array de amostras. As amostras devem ser de 16-bit, 
     * sinalizadas e no formato "little-endian". 
     * Esse m�todo deve ser implementado pelas subclasses. Aten��o, o offset e 
     * o tamanho referen-se ao n�mero de bytes e n�o amostras.
     */
    public abstract void filter(
            byte[] samples, int offset, int length );
    
    
    /**
     * M�todo de conveni�ncia para obter uma amostra de 16-bit de um 
     * array de bytes. As amostras devem ser de 16-bit, 
     * sinalizadas e no formato "little-endian".
     */
    public static short getSample( byte[] buffer, int position ) {
        return ( short ) (
                ( ( buffer[ position + 1 ] & 0xff ) << 8 ) |
                ( buffer[ position ] & 0xff ) );
    }
    
    
    /**
     * M�todo de conveni�ncia para configurar uma amostra de 16-bit em um array 
     * de bytes. As amostras devem ser de 16-bit, 
     * sinalizadas e no formato "little-endian".
     */
    public static void setSample( byte[] buffer, int position,
            short sample ) {
        buffer[ position ] = ( byte ) ( sample & 0xff );
        buffer[ position + 1 ] = ( byte ) ( ( sample >> 8 ) & 0xff );
    }
    
}