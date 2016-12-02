package infraestrutura.som;

/**
 * A classe EchoFilter � um SoundFilter que emula um �co.
 *
 * @see FilteredSoundStream
 *
 * @author David Buzatto
 */
public class EchoFilter extends SoundFilter {
    
    private short[] delayBuffer;
    private int delayBufferPos;
    private float decay;
    
    
    /**
     * Cria um EchoFilter com o n�mero especificado de atrasos e com a 
     * taxa de decaimento.
     * <p>O n�mero de atrasos especifica por quanto tempo o �co ser� ouvido 
     * inicialmente. Para um �co de 1 segundos com mono, som de 44100Hz, 
     * use 44100 de atraso.
     * <p>O valor de decaimento � o quanto o �co decai a partir da fonte. 
     * Um valor de deciamento de .5 siginifica que o �co � ouvido com a metada 
     * da intensidade do som da fonte.
     */
    public EchoFilter( int numDelaySamples, float decay ) {
        delayBuffer = new short[ numDelaySamples ];
        this.decay = decay;
    }
    
    
    /**
     * Obt�m o tamanho restante, em bytes, das amostrar que esse filtro pode 
     * ecoar antes que o som termine de ser exeuctado.
     * Certifica que o som ser� deca�do mens que 1% do volume m�ximo (amplitude).
     */
    public int getRemainingSize() {
        
        float finalDecay = 0.01f;
        
        // derivado de Math.pow(decay,x) <= finalDecay
        int numRemainingBuffers = ( int ) Math.ceil(
                Math.log( finalDecay ) / Math.log( decay ) );
        int bufferSize = delayBuffer.length * 2;
        
        return bufferSize * numRemainingBuffers;
    }
    
    
    /**
     * Limpa o buffer interno de atrasos do EchoFilter.
     */
    public void reset() {
        for ( int i = 0; i < delayBuffer.length; i++ ) {
            delayBuffer[ i ] = 0;
        }
        delayBufferPos = 0;
    }
    
    
    /**
     * Filtra as amostras de som apra adicionar o eco. As amostras
     * tocadas s�o adicionadas ao som do buffer de atraso multiplicadas pela 
     * taxa de decaimento. O resultado � armazenado no buffer de atraso, ent�o 
     * m�ltiplos ecos s�o ouvidos.
     */
    public void filter(byte[] samples, int offset, int length) {
        
        for ( int i = offset; i < offset + length; i += 2 ) {
            
            // atualiza a amostra
            short oldSample = getSample( samples, i );
            short newSample = ( short ) ( oldSample + decay *
                    delayBuffer[ delayBufferPos ] );
            setSample( samples, i, newSample );
            
            // atualiza o buffer de atrasos
            delayBuffer[ delayBufferPos ] = newSample;
            delayBufferPos++;
            if ( delayBufferPos == delayBuffer.length ) {
                delayBufferPos = 0;
            }
        }
    }
    
}