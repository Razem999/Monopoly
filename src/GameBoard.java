import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameBoard {
    List<GameTileI> tiles;

    GameBoard() {
        this.tiles = new ArrayList<>();

        for (int i = 0; i < 40; i++) {
            this.tiles.add(new GoTile());
        }
    }

    Optional<String> getTileDescriptionByIndex(int index) {
        if (this.tiles.size() <= index) {
            return Optional.empty();
        }

        return Optional.of("Tile " +
                index +
                " is " +
                this.tiles.get(index).tileDescription());
    }
}
