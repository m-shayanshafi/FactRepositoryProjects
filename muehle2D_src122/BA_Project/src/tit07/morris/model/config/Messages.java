package tit07.morris.model.config;


public class Messages {

    private Messages() {

    }

    public static String getString( String key ) {
        return Language.getString( key );
    }
}
