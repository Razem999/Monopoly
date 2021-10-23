import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameBoard {
    private final List<GameTileI> tiles;
    private final GameInterfaceI gameInterface;
    private final List<PropertyTile> propertyTiles;

    GameBoard(GameInterfaceI gameInterface) {
        this.gameInterface = gameInterface;
        this.tiles = new ArrayList<>();
        this.propertyTiles = new ArrayList<>();
        propertyTiles.addAll(PropertyTileBuilder.createTiles(gameInterface));

        this.tiles.add(new GoTile(gameInterface));
        this.tiles.add(propertyTiles.get(0));
        this.tiles.add(new EmptyTile());
        this.tiles.add(propertyTiles.get(1));
        this.tiles.add(new IncomeTaxTile(gameInterface));
        this.tiles.add(new Railroad("Reading Railroad", gameInterface, 200));
        this.tiles.add(propertyTiles.get(2));
        this.tiles.add(new EmptyTile());
        this.tiles.add(propertyTiles.get(3));
        this.tiles.add(propertyTiles.get(4));
        this.tiles.add(new EmptyTile()); //Jail
        this.tiles.add(propertyTiles.get(5));
        this.tiles.add(new UtilityTile("Electric Company", 150, gameInterface));
        this.tiles.add(propertyTiles.get(6));
        this.tiles.add(propertyTiles.get(7));
        this.tiles.add(new Railroad("Pennsylvania Railroad", gameInterface, 200));
        this.tiles.add(propertyTiles.get(8));
        this.tiles.add(new EmptyTile());
        this.tiles.add(propertyTiles.get(9));
        this.tiles.add(propertyTiles.get(10));
        this.tiles.add(new EmptyTile()); //Free Parking
        this.tiles.add(propertyTiles.get(11));
        this.tiles.add(new EmptyTile());
        this.tiles.add(propertyTiles.get(12));
        this.tiles.add(propertyTiles.get(13));
        this.tiles.add(new Railroad("B. & O. Railroad", gameInterface, 200));
        this.tiles.add(propertyTiles.get(14));
        this.tiles.add(propertyTiles.get(15));
        this.tiles.add(new UtilityTile("Water Works", 150, gameInterface));
        this.tiles.add(propertyTiles.get(16));
        this.tiles.add(new GoJail(gameInterface));
        this.tiles.add(propertyTiles.get(17));
        this.tiles.add(propertyTiles.get(18));
        this.tiles.add(new EmptyTile());
        this.tiles.add(propertyTiles.get(19));
        this.tiles.add(new Railroad("Short Line Railroad", gameInterface, 200));
        this.tiles.add(new EmptyTile());
        this.tiles.add(propertyTiles.get(20));
        this.tiles.add(new LuxuryTaxTile(gameInterface));
        this.tiles.add(propertyTiles.get(21));
    }

    public void sendPlayerToJail(Player player) {}

    public List<GameTileI> getTilesOwnedByPlayer(Player player) {
        return this.tiles.stream().filter((GameTileI tile) -> tile.isOwnedBy(player)).collect(Collectors.toList());
    }

    public void advancePlayer(Player player, int tiles, Players players) {
        int unadjustedPosition = player.getTilePosition() + tiles;
        boolean didPassGo = unadjustedPosition > this.tiles.size();
        int adjustedPosition = unadjustedPosition % this.tiles.size();
        player.setTilePosition(adjustedPosition);

        if (didPassGo) {
            gameInterface.notifyPassGo(player);
            player.changeBalance(GoTile.passReward);
        }

        if (adjustedPosition < this.tiles.size()) {
            GameTileI tile = this.tiles.get(adjustedPosition);

            gameInterface.notifyPlayerMovement(player, tiles, adjustedPosition, tile.tileDescription());
            tile.onLand(player, this, players);
        } else {
            System.out.println("ERROR LANDED ON UNKNOWN TILE, CHECK GAME BOARD FILE");
        }
    }

    public Optional<GameTileI> getTile(int index) {
        if (index < this.tiles.size()) {
            return Optional.of(this.tiles.get(index));
        }

        return Optional.empty();
    }

    public Optional<String> getTileDescriptionByIndex(int index) {
        if (this.tiles.size() <= index) {
            return Optional.empty();
        }

        return Optional.of("Tile " +
                index + "\n" +
                this.tiles.get(index).tileDescription());
    }

    public void transferPlayerProperties(Player source, Player destination) {
        for (GameTileI gameTile : this.getTilesOwnedByPlayer(source)) {
            gameTile.tryTransferOwnership(destination);
        }
    }

    public List<GameTileI> getPropertiesFilter(TileFilter tileFilter) {
        List<GameTileI> result = new ArrayList<>();
        for (GameTileI tile : this.tiles) {
            if (tileFilter.filter(tile)) {
                result.add(tile);
            }
        }
        return result;
    }
}
