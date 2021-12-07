package save;

import java.io.Serializable;

public class PropertyTileSave implements Serializable {
    private final int propertyID;
    private final int ownerID;
    private final int houses;
    private final boolean hasHotel;

    public PropertyTileSave(int propertyID, int ownerID, int houses, boolean hasHotel) {
        this.propertyID = propertyID;
        this.ownerID = ownerID;
        this.houses = houses;
        this.hasHotel = hasHotel;
    }

    public int getPropertyID() {
        return propertyID;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public int getHouses() {
        return houses;
    }

    public boolean getHasHotel() {
        return hasHotel;
    }
}
