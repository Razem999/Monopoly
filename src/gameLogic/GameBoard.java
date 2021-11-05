package gameLogic;

import gameInterface.GameInterfaceI;
import gameInterface.GameTileDrawable;
import tiles.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The GameBoard class represents the entirety of the monopoly gameboard where all types of tiles are put together
 */
public class GameBoard {

    private final List<GameTileI> tiles;
    private final GameInterfaceI gameInterface;
    private final int jailIndex;
    private final JailTile jailTile;

    /**This is the constructor of GameBoard.
     */
    public GameBoard(GameInterfaceI gameInterface) {
        this.gameInterface = gameInterface;
        this.jailIndex = 10;
        this.jailTile = new JailTile();

        FreeParking freeParking = new FreeParking(gameInterface);
        IncomeTaxTile incomeTaxTile = new IncomeTaxTile(gameInterface, freeParking);
        LuxuryTaxTile luxuryTaxTile = new LuxuryTaxTile(gameInterface, freeParking);
        this.tiles = new ArrayList<>();
        List<PropertyTile> propertyTiles = new ArrayList<>(PropertyTileBuilder.createTiles(gameInterface));

        this.tiles.add(new GoTile(gameInterface));
        this.tiles.add(propertyTiles.get(0));
        this.tiles.add(new EmptyTile());
        this.tiles.add(propertyTiles.get(1));
        this.tiles.add(incomeTaxTile);

        this.tiles.add(new RailroadTile("Reading Railroad", gameInterface, 200));
        this.tiles.add(propertyTiles.get(2));
        this.tiles.add(new EmptyTile());
        this.tiles.add(propertyTiles.get(3));
        this.tiles.add(propertyTiles.get(4));

        this.tiles.add(this.jailTile);
        this.tiles.add(propertyTiles.get(5));
        this.tiles.add(new UtilityTile("Electric Company", 150, gameInterface));
        this.tiles.add(propertyTiles.get(6));
        this.tiles.add(propertyTiles.get(7));

        this.tiles.add(new RailroadTile("Pennsylvania Railroad", gameInterface, 200));
        this.tiles.add(propertyTiles.get(8));
        this.tiles.add(new EmptyTile());
        this.tiles.add(propertyTiles.get(9));
        this.tiles.add(propertyTiles.get(10));
        this.tiles.add(freeParking);
        this.tiles.add(propertyTiles.get(11));
        this.tiles.add(new EmptyTile());
        this.tiles.add(propertyTiles.get(12));
        this.tiles.add(propertyTiles.get(13));
        this.tiles.add(new RailroadTile("B. & O. Railroad", gameInterface, 200));
        this.tiles.add(propertyTiles.get(14));
        this.tiles.add(propertyTiles.get(15));
        this.tiles.add(new UtilityTile("Water Works", 150, gameInterface));
        this.tiles.add(propertyTiles.get(16));
        this.tiles.add(new GoJail());
        this.tiles.add(propertyTiles.get(17));
        this.tiles.add(propertyTiles.get(18));
        this.tiles.add(new EmptyTile());
        this.tiles.add(propertyTiles.get(19));
        this.tiles.add(new RailroadTile("Short Line Railroad", gameInterface, 200));
        this.tiles.add(new EmptyTile());
        this.tiles.add(propertyTiles.get(20));
        this.tiles.add(luxuryTaxTile);
        this.tiles.add(propertyTiles.get(21));
    }

    /**This method gets all tiles owned by a specific player
     * @param player This provides the player who's owned properties will be displayed
     */
    public List<BuyableI> getTilesOwnedByPlayer(Player player) {
        return this.tiles.stream().filter((GameTileI tile) -> {
            Optional<BuyableI> buyableTile = tile.asBuyable();
            return buyableTile.map(buyable -> buyable.isOwnedBy(player)).orElse(false);
        }).map((GameTileI tile) -> tile.asBuyable().orElseThrow()).collect(Collectors.toList());
    }

    /**This method moves a player around the board
     * @param player This provides the player currently being moved
     * @param tiles This provides the number of tiles a player is moving
     * @param players This provides a list of all players in the game
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

    /**This method gets a tile using its index in the tiles array
     * @param index This provides the index of the desired tile
     */
    public Optional<GameTileI> getTile(int index) {
        if (index < this.tiles.size()) {
            return Optional.of(this.tiles.get(index));
        }

        return Optional.empty();
    }

    /**This method gets the description of a specific tile using its index
     * @param index This provides the index of the desired tile
     */
    public Optional<String> getTileDescriptionByIndex(int index) {
        if (this.tiles.size() <= index) {
            return Optional.empty();
        }

        return Optional.of("Tile " +
                index + "\n" +
                this.tiles.get(index).tileDescription());
    }

    /**This method transfers all properties from one player to another (Used in bankruptcy case)
     * @param source This provides the player who the properties are being transferred from
     * @param destination This provides the player who the properties are being transferred to
     */
    public void transferPlayerProperties(Player source, Player destination) {
        for (BuyableI buyableTile : this.getTilesOwnedByPlayer(source)) {
            buyableTile.transferOwnership(destination);
        }
    }

    /**This method filters through all tiles selecting a certain type of tile
     * @param tileFilter This provides the type of filter that determine which type of tile the function looks for
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

    /**This method gets the player in jail to pay the jail release fee
     * @param player This provides the player currently paying to get out of jail
     */
    public void payJailFine(Player player) {
        gameInterface.notifyPlayerLeftJail(player);
        player.changeBalance(-1 * (JailTile.jailFine));
        this.jailTile.unjailPlayer(player);
    }

    /**This method jails a player
     * @param player This provides the player currently going to jail
     */
    public void jailPlayer(Player player) {
        gameInterface.notifyPlayerSentToJail(player);
        player.setTilePosition(this.jailIndex);
        this.jailTile.jailPlayer(player);
    }

    /**This method returns whether a player is in jail or not
     * @param player This provides the player in question
     */
    public boolean isPlayerInJail(Player player) {
        return this.jailTile.isPlayerJailed(player);
    }

    /**This method handles a roll that does not free a jailed player immediately
     * @param player This provides the player in question
     */
    public void handleFailedJailedPlayerRoll(Player player) {
        jailTile.incrementPlayerRolls(player);
        if (jailTile.hasPlayerRolledOutOfJail(player)) {
            this.payJailFine(player);
        } else {
            gameInterface.notifyPlayerStayJail(player);
        }
    }

    /**This method handles a roll that frees a jailed player immediately
     * @param player This provides the player in question
     */
    public void handleSuccessfulJailedPlayerRoll(Player player) {
        gameInterface.notifyPlayerLeftJail(player);
        jailTile.unjailPlayer(player);
    }

    public List<GameTileDrawable> getTileDrawables() {
        return IntStream.range(0, this.tiles.size())
                .mapToObj(i -> new GameTileDrawable(this.tiles.get(i), i)).collect(Collectors.toList());
    }

}
