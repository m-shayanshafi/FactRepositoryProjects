package tit07.morris.controller.ai;

import tit07.morris.model.config.Language;

public class Messages {

    private Messages() {

    }

    public static String getString( String key ) {
        return Language.getString( key );
    }
}
