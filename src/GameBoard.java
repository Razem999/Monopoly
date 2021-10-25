import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The GameBoard class represents the combination of all tiles into the gameboard
 * that players move around. This determines the order of tiles and the methods
 * to get information about each tile on the board.
 *
 * @version 1.0
 * @since 2021-10-25
 */
public class GameBoard {

    private final List<GameTileI> tiles;
    private final GameInterfaceI gameInterface;
    private final List<PropertyTile> propertyTiles;

    /**This is the constructor of GameBoard with a parameter
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

    /**This function sends the player to the jail tile
     * @param player This provides the player who landed on the tile
     */
    public void sendPlayerToJail(Player player) {
        gameInterface.notifyPlayerSentToJail(player);
        player.setTilePosition(10);
        player.toggleInJail();
    }

    /**This function sends a list of all the players owned properties
     * @param player This provides the player requested this operation
     */
    public List<GameTileI> getTilesOwnedByPlayer(Player player) {
        return this.tiles.stream().filter((GameTileI tile) -> tile.isOwnedBy(player)).collect(Collectors.toList());
    }

    /**This function advances the player a certain number of tiles around the gameboard
     * @param player This provides the player who is moving
     * @param tiles This provides the number of tiles the player will advance
     * @param players This provides the list of all players
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

    /**This function gets a specified tile
     * @param index this in the position in the ArrayList tiles that desired tile is at
     */
    public Optional<GameTileI> getTile(int index) {
        if (index < this.tiles.size()) {
            return Optional.of(this.tiles.get(index));
        }

        return Optional.empty();
    }

    /**This function gets a specified tile's description
     * @param index this in the position in the ArrayList tiles that desired tile is at
     */
    public Optional<String> getTileDescriptionByIndex(int index) {
        if (this.tiles.size() <= index) {
            return Optional.empty();
        }

        return Optional.of("Tile " +
                index + "\n" +
                this.tiles.get(index).tileDescription());
    }

    /**This function transfers properties after bankruptcy from one player to another
     * @param source this is the player who has the property in the beginning
     * @param destination this is the player who will receive the property from the source player
     */
    public void transferPlayerProperties(Player source, Player destination) {
        for (GameTileI gameTile : this.getTilesOwnedByPlayer(source)) {
            gameTile.tryTransferOwnership(destination);
        }
    }

    /**This function filters all tiles and returns a list of the desired tiles type
     * @param tileFilter this is the type of filter being used to determine which property types to find
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

    /**This function makes the player pay the bank to get out of jail
     * @param player this is the player currently in jail
     */
    public void payJailFine(Player player) {
        player.changeBalance(-1 * (Jail.jailFine));
    }
}
