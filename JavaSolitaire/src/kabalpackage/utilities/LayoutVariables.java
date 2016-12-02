package kabalpackage.utilities;

import java.awt.*;

/**
 * This class just contains constants that define various layout settings,
 * such as positioning/margins, sizes and colors.
 */
public class LayoutVariables {
    
    // Window size
    public static final int WINDOW_WIDTH = 800;
    public static final int  WINDOW_HEIGHT = 600;
    
    // Number of stacks
    public static final int SOLITAIRE_STACK_COUNT = 7;
    public static final int FOUNDATION_COUNT = 4;
    
    // Spacing between cards - horisontal and vertical
    public static final int CARD_SPACING = 25;
    public static final int CARD_YSPACING = 20;
    
    // Horisontal spacing between the stacks
    public static final int STACK_SPACING = 20;
    
    // Vertical spacing between the top and bottom stacks
    public static final int SECTION_SPACING = 20;
    
    // Card dimensions
    public static final int CARD_WIDTH = 79;
    public static final int CARD_HEIGHT = 123;
    
    // Left margin and horisontal start position of the Foundations
    public static final int MARGIN_LEFT = 60;
    public static final int FOUNDATION_XPOS_START = MARGIN_LEFT + 
            ((SOLITAIRE_STACK_COUNT-FOUNDATION_COUNT)*CARD_WIDTH)
            + ((SOLITAIRE_STACK_COUNT-FOUNDATION_COUNT)*STACK_SPACING);
    
    // Vertical margin and vertical start position of SolitaireStacks
    public static final int FOUNDATION_YPOS_START = 35;
    public static final int STACK_YPOS_START = FOUNDATION_YPOS_START
            + SECTION_SPACING + CARD_HEIGHT;
    
    // Colors and transparency
    public static final Color BACKGROUND_COLOR = new Color(55, 95, 20);
    public static final Color PLACEHOLDER_COLOR = Color.WHITE;
    public static final AlphaComposite PLACEHOLDER_ALPHA = 
            AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.05f);
    public static final AlphaComposite PLACEHOLDER_ALPHA_HIGHLIGHT = 
            AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f);
    
    // Background images - names to display in menu
    public static final String[] bgNames = { 
        "Standard green",
        "Standard blue",
        "Blue boxes",
        "Green boxes",
        "Pleasant",
        "Abstract (blue)",
        "Flowers (blue)"
    };
    
    // Background images - file names
    public static final String[] fileNames = { 
        "bgstandardgreen.png", 
        "bgstandardblue.png", 
        "bgblueboxes.png", 
        "bggreenboxes.png", 
        "bgpleasant.png", 
        "bgabstractblue.png",
        "bgflowersblue.png"
    };
    
    
    // Card styles
    public static final String[] cardTitles = {
        "Bonded (Gnome)",
        "Dondorf (Gnome)",
        "Pondus"
    };
    
    public static final String[] cardFiles = {
        "bondedcards.png",
        "dondorf.png",
        "pondus.png"
    };
    
    public static final String[] cardOverFiles = {
        "bondedcards-over.png",
        "dondorf-over.png",
        "pondus-over.png"
    };
    
}
