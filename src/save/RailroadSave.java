package save;

import java.io.Serializable;

public class RailroadSave implements Serializable {
    private final int railroadID;
    private final int ownerID;

    public RailroadSave(int railroadID, int ownerID) {
        this.railroadID = railroadID;
        this.ownerID = ownerID;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public int getRailroadID() {
        return railroadID;
    }
}
