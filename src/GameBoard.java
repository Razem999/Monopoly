import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameBoard {
    private final List<GameTileI> tiles;
    private final GameInterfaceI gameInterface;

    GameBoard(GameInterfaceI gameInterface) {
        this.gameInterface = gameInterface;
        this.tiles = new ArrayList<>();

        this.tiles.add(new GoTile());
        this.tiles.addAll(PropertyTileBuilder.createTiles(gameInterface));
    }

    public void sendPlayerToJail(Player player) {}

    public void advancePlayer(Player player, int tiles, Players players) {
        int unadjustedPosition = player.getTilePosition() + tiles;
        boolean didPassGo = unadjustedPosition > this.tiles.size();
        int adjustedPosition = unadjustedPosition % this.tiles.size();
        player.setTilePosition(adjustedPosition);

        if (didPassGo) {
            gameInterface.notifyPassGo(player);
            player.changeBalance(GoTile.PassReward);
        }

        if (adjustedPosition < this.tiles.size()) {
            GameTileI tile = this.tiles.get(adjustedPosition);

            gameInterface.notifyPlayerMovement(player, tiles, adjustedPosition, tile.tileDescription());
            tile.onLand(player, this, players);
        } else {
            System.out.println("ERROR LANDED ON UNKNOWN TILE, CHECK GAME BOARD FILE");
        }
    }

    Optional<String> getTileDescriptionByIndex(int index) {
        if (this.tiles.size() <= index) {
            return Optional.empty();
        }

        return Optional.of("Tile " +
                index + "\n" +
                this.tiles.get(index).tileDescription());
    }
}
