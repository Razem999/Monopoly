package save;

import java.io.Serializable;

public class GameBoardSave implements Serializable {
    private final int placeableHouses;
    private final int placeableHotels;

    public GameBoardSave(int placeableHouses, int placeableHotels) {
        this.placeableHouses = placeableHouses;
        this.placeableHotels = placeableHotels;
    }

    public int getPlaceableHotels() {
        return placeableHotels;
    }

    public int getPlaceableHouses() {
        return placeableHouses;
    }
}
