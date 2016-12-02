package tit07.morris.model.config;

import java.util.Locale;
import java.util.ResourceBundle;


public class Language {

    private static ResourceBundle messages;
    
    public static void setLanguage(Locale locale) {        
        messages = ResourceBundle.getBundle("messages", //$NON-NLS-1$
                                           locale);
    }
    
    
    public static String getString( String key ) {        
        return messages.getString( key );
    }
}
