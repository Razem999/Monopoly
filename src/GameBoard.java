import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The GameBoard is the map where all the tiles are visible to the Player. This
 * is where the tiles are arranged in order and all the tiles are initialized.
 */
public class GameBoard {

    private final List<GameTileI> tiles;
    private final GameInterfaceI gameInterface;
    private final List<PropertyTile> propertyTiles;

    /**This is the constructor of GameBoard, where all the properties are initialized
     * and ordered. The constructor takes in a parameter
     * @param gameInterface This provides text for each action the player takes
     */
    GameBoard(GameInterfaceI gameInterface) {
        FreeParking freeParking = new FreeParking(gameInterface);
        IncomeTaxTile incomeTaxTile = new IncomeTaxTile(gameInterface, freeParking);
        LuxuryTaxTile luxuryTaxTile = new LuxuryTaxTile(gameInterface, freeParking);
        this.gameInterface = gameInterface;
        this.tiles = new ArrayList<>();
        this.propertyTiles = new ArrayList<>();
        propertyTiles.addAll(PropertyTileBuilder.createTiles(gameInterface));
        this.tiles.add(new GoTile(gameInterface));
        this.tiles.add(propertyTiles.get(0));
        this.tiles.add(new EmptyTile());
        this.tiles.add(propertyTiles.get(1));
        this.tiles.add(incomeTaxTile);
        this.tiles.add(new Railroad("Reading Railroad", gameInterface, 200));
        this.tiles.add(propertyTiles.get(2));
        this.tiles.add(new EmptyTile());
        this.tiles.add(propertyTiles.get(3));
        this.tiles.add(propertyTiles.get(4));
        this.tiles.add(new Jail(gameInterface));
        this.tiles.add(propertyTiles.get(5));
        this.tiles.add(new UtilityTile("Electric Company", 150, gameInterface));
        this.tiles.add(propertyTiles.get(6));
        this.tiles.add(propertyTiles.get(7));
        this.tiles.add(new Railroad("Pennsylvania Railroad", gameInterface, 200));
        this.tiles.add(propertyTiles.get(8));
        this.tiles.add(new EmptyTile());
        this.tiles.add(propertyTiles.get(9));
        this.tiles.add(propertyTiles.get(10));
        this.tiles.add(freeParking);
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
        this.tiles.add(luxuryTaxTile);
        this.tiles.add(propertyTiles.get(21));
    }

    /**This method is used to change the Player's position to the Jail tile.
     * @param player This is the Player sent to Jail
     */
    public void sendPlayerToJail(Player player) {
        gameInterface.notifyPlayerSentToJail(player);
        player.setTilePosition(10);
        player.toggleInJail();
    }

    /**This method is used to get the tiles owned by a Player
     * @param player This is the Player who owns the tiles
     * @return This returns a list of tiles the Player owns
     */
    public List<GameTileI> getTilesOwnedByPlayer(Player player) {
        return this.tiles.stream().filter((GameTileI tile) -> tile.isOwnedBy(player)).collect(Collectors.toList());
    }

    /**This method is used to move the Player from their old position to
     * the new position.
     * @param player This is the Player who is moving
     * @param tiles This is the number of tiles the Player has moved
     * @param players These are list of Players who owns properties, where a Player could land on
     */
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

    /**This method is used to get the tile at a specified index/position.
     * @param index This is the position of the tile
     * @return This returns the object at that tile
     */
    public Optional<GameTileI> getTile(int index) {
        if (index < this.tiles.size()) {
            return Optional.of(this.tiles.get(index));
        }

        return Optional.empty();
    }

    /**This method is used to get the description of a specified tile.
     * @param index This is the index of the tile the description is being retrieved from
     * @return This returns the description of the tile in the specified index
     */
    public Optional<String> getTileDescriptionByIndex(int index) {
        if (this.tiles.size() <= index) {
            return Optional.empty();
        }

        return Optional.of("Tile " +
                index + "\n" +
                this.tiles.get(index).tileDescription());
    }

    /**This method is used to transfer properties between Players.
     * @param source This is the Player who is giving the properties
     * @param destination This is the Player who is receiving the properties
     */
    public void transferPlayerProperties(Player source, Player destination) {
        for (GameTileI gameTile : this.getTilesOwnedByPlayer(source)) {
            gameTile.tryTransferOwnership(destination);
        }
    }

    /**This method is used to filter the properties to their appropriate sets
     * @param tileFilter This is the interface that arranges the tiles to their sets
     * @return This returns the list of tiles in a specific set
     */
    public List<GameTileI> getPropertiesFilter(TileFilter tileFilter) {
        List<GameTileI> result = new ArrayList<>();
        for (GameTileI tile : this.tiles) {
            if (tileFilter.filter(tile)) {
                result.add(tile);
            }
        }
        return result;
    }

    /**This method is used when a Player leaves Jail by paying the Jail fine.
     * @param player This is the Player leaving Jail
     */
    public void payJailFine(Player player) {
        player.changeBalance(-1 * (Jail.jailFine));
    }
}
