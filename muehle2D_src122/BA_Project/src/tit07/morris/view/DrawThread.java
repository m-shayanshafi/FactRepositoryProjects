package tit07.morris.view;

/**
 * Die Klasse DrawThread implementiert den Zeichenthread, welcher in einem
 * bestimmten Zeitintervall das Spielfeld neu zeichnet und gegebenenfalls die
 * Animation bewegt.
 */
public class DrawThread implements Runnable {

    /** Referenz auf den View */
    private Drawable view;

    /** Aktualisierungsrate für 25 FPS (1000ms / 25Frames = 40ms) */
    private int      sleepTime = 40;

    /** Variable für die Threadverwaltung */
    private Thread   thread;


    /**
     * Erzeugt eine neue Instanz des Zeichenthreads
     * 
     * @param view Referenz auf den View
     */
    public DrawThread( Drawable view ) {

        this.view = view;
    }

    /**
     * Startet den Zeichenthread
     */
    public void start() {

        this.thread = new Thread( this,
                                  "Draw-Thread" ); //$NON-NLS-1$
        this.thread.setDaemon( true );
        this.thread.start();
    }

    /**
     * Stoppt den Zeichenthread
     */
    public void stop() {

        thread = null;
    }

    /**
     * Zeichnet die Spielfläche in einem bestimmten Intervall neu. Die Zeit für
     * das zeichnen wird berücksichtigt, so dass die Framerate möglichst
     * konstant bleibt.
     */
    public void run() {

        try {
            Thread.sleep( 100 );
        }
        catch( InterruptedException e ) {
        }
        while( Thread.currentThread() == thread ) {
            long startTime = System.currentTimeMillis();
            this.repaint( true );
            long endTime = System.currentTimeMillis();
            long realSleepTime = ( sleepTime - endTime + startTime > 0 ) ? ( sleepTime
                                                                                - endTime + startTime )
                                                                        : sleepTime;
            try {
                Thread.sleep( realSleepTime );
            }
            catch( InterruptedException e ) {
            }
        }
    }

    /**
     * Zeichnet die gesamte Spielfläche neu.
     * 
     * @param moveAnimation Gibt an, ob die Animation fortgesetzt werden soll.
     */
    public synchronized void repaint( boolean moveAnimation ) {

        /* Zeichne Spielfläche */
        this.view.repaintGameWindow();

        /* Setze Animation fort */
        if( moveAnimation ) {

            /* Animation zu Ende: Aufräumarbeiten */
            if( view.moveAnimation() ) {
                int position = view.getAnimationDestination();
                this.view.disposeAnimation( position );
                this.view.repaintGameWindow();
            }
        }

        /* Status Window neu zeichnen */
        this.view.updateStatusWindow( view.getWidth() );
        this.view.repaintStatusWindow();

        /* Menü neu zeichnen */
        this.view.repaintTopMenu();
    }
}
