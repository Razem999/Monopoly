package gameInterface;

import java.util.HashMap;
import java.util.Map;

public class LanguageConfiguration {

    private static LanguageConfiguration instance;

    public static LanguageConfiguration getInstance() {
        if (LanguageConfiguration.instance == null) {
            instance = new LanguageConfiguration();
        }
        return instance;
    }

    public Map<String, String> getStreetNames() {
        return new HashMap<>();
    }

}