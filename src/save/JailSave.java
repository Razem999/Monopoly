package save;

import java.io.Serializable;
import java.util.Map;

public class JailSave implements Serializable {
    private final Map<Integer, Integer> jailedPlayers;

    public JailSave(Map<Integer, Integer> jailedPlayers) {
        this.jailedPlayers = jailedPlayers;
    }

    public Map<Integer, Integer> getJailedPlayers() {
        return jailedPlayers;
    }
}
