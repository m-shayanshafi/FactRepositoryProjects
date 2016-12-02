package infraestrutura.som;

/**
 * A classe Sound � um container para amostras de som. As amostras de som s�o de
 * formato desconhecido e armazenadas como um array de bytes.
 *
 * @author David Buzatto
 */
public class Sound {
    
    private byte[] samples;
    
    /**
     * Cria um novo objeto Sound com o array de bytes especificado.
     * O array n�o � copiado.
     */
    public Sound( byte[] samples ) {
        this.samples = samples;
    }
    
    
    /**
     * Retorna esse objeto Sound como um array de byres.
     */
    public byte[] getSamples() {
        return samples;
    }
    
}