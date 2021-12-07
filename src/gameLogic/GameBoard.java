package gameLogic;

import gameInterface.GameInterface;
import gameInterface.GameTileDrawable;
import gameInterface.LanguageConfiguration;
import tiles.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The GameBoard class represents the entirety of the monopoly gameboard where all types of tiles are put together
 */
public class GameBoard {

    public static record TileAndIndex(GameTile tile, int index) { }

    private final  int totalHouses = 32;
    private final  int totalHotels = 12;
    private final List<GameTile> tiles;
    private final GameInterface gameInterface;
    private final int jailIndex;
    private final JailTile jailTile;
    private int placeableHouses;
    private int placeableHotels;


    public List<GameTile> getTiles() {
        return this.tiles;
    }
    /**This is the constructor of GameBoard.
     */
    public GameBoard(GameInterface gameInterface) {
        this.placeableHouses = 32;
        this.placeableHotels = 12;
        this.gameInterface = gameInterface;
        this.jailIndex = 10;
        this.jailTile = new JailTile();

        Map<String, String> streetNames = LanguageConfiguration.getInstance().getStreetNames();

        FreeParking freeParking = new FreeParking(gameInterface);
        IncomeTaxTile incomeTaxTile = new IncomeTaxTile(gameInterface, freeParking);
        LuxuryTaxTile luxuryTaxTile = new LuxuryTaxTile(gameInterface, freeParking);
        this.tiles = new ArrayList<>();
        List<PropertyTile> propertyTiles = new ArrayList<>(PropertyTileBuilder.createTiles(gameInterface));

        this.tiles.add(new GoTile(gameInterface));  //0
        this.tiles.add(propertyTiles.get(0));       //1
        this.tiles.add(new EmptyTile());            //2
        this.tiles.add(propertyTiles.get(1));       //3
        this.tiles.add(incomeTaxTile);              //4

        this.tiles.add(new RailroadTile(streetNames.get("readingRailroad"), gameInterface, 200));   //5
        this.tiles.add(propertyTiles.get(2));       //6
        this.tiles.add(new EmptyTile());            //7
        this.tiles.add(propertyTiles.get(3));       //8
        this.tiles.add(propertyTiles.get(4));       //9

        this.tiles.add(this.jailTile);              //10
        this.tiles.add(propertyTiles.get(5));       //11
        this.tiles.add(new UtilityTile(streetNames.get("electricCompany"), 150, gameInterface));    //12
        this.tiles.add(propertyTiles.get(6));       //13
        this.tiles.add(propertyTiles.get(7));       //14

        this.tiles.add(new RailroadTile(streetNames.get("pennsylvaniaRailroad"), gameInterface, 200));  //15
        this.tiles.add(propertyTiles.get(8));       //16
        this.tiles.add(new EmptyTile());            //17
        this.tiles.add(propertyTiles.get(9));       //18
        this.tiles.add(propertyTiles.get(10));      //19
        this.tiles.add(freeParking);                //20
        this.tiles.add(propertyTiles.get(11));      //21
        this.tiles.add(new EmptyTile());            //22
        this.tiles.add(propertyTiles.get(12));      //23
        this.tiles.add(propertyTiles.get(13));      //24
        this.tiles.add(new RailroadTile(streetNames.get("bORailroad"), gameInterface, 200));   //25
        this.tiles.add(propertyTiles.get(14));      //26
        this.tiles.add(propertyTiles.get(15));      //27
        this.tiles.add(new UtilityTile(streetNames.get("waterWorks"), 150, gameInterface)); //28
        this.tiles.add(propertyTiles.get(16));      //29
        this.tiles.add(new GoJail());               //30
        this.tiles.add(propertyTiles.get(17));      //31
        this.tiles.add(propertyTiles.get(18));      //32
        this.tiles.add(new EmptyTile());            //33
        this.tiles.add(propertyTiles.get(19));      //34
        this.tiles.add(new RailroadTile(streetNames.get("shortLineRailroad"), gameInterface, 200));    //35
        this.tiles.add(new EmptyTile());            //36
        this.tiles.add(propertyTiles.get(20));      //37
        this.tiles.add(luxuryTaxTile);              //38
        this.tiles.add(propertyTiles.get(21));      //39
    }

    /**This method gets all tiles owned by a specific player
     * @param player This provides the player who's owned properties will be displayed
     */
    public List<BuyableTile> getTilesOwnedByPlayer(Player player) {
        return this.tiles.stream().filter((GameTile tile) -> {
            Optional<BuyableTile> buyableTile = tile.asBuyable();
            return buyableTile.map(buyable -> buyable.isOwnedBy(player)).orElse(false);
        }).map((GameTile tile) -> tile.asBuyable().orElseThrow()).collect(Collectors.toList());
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
            GameTile tile = this.tiles.get(adjustedPosition);

            gameInterface.notifyPlayerMovement(player, tiles, adjustedPosition, tile.tileDescription());
            tile.onLand(player, this, players);
        } else {
            System.out.println("ERROR LANDED ON UNKNOWN TILE, CHECK GAME BOARD FILE");
        }
    }

    /**This method gets a tile using its index in the tiles array
     * @param index This provides the index of the desired tile
     */
    public Optional<GameTile> getTile(int index) {
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
        for (BuyableTile buyableTile : this.getTilesOwnedByPlayer(source)) {
            buyableTile.transferOwnership(destination);
        }
    }

    /**This method filters through all tiles selecting a certain type of tile
     * @param tileFilter This provides the type of filter that determine which type of tile the function looks for
     */
    public List<GameTile> getPropertiesFilter(TileFilter tileFilter) {
        List<GameTile> result = new ArrayList<>();
        for (GameTile tile : this.tiles) {
            if (tileFilter.filter(tile)) {
                result.add(tile);
            }
        }
        return result;
    }

    /**This method is used to create a list of game tiles with an index
     * @param p a function that sorts gameTiles
     */
    public List<TileAndIndex> filterTilesWithIndex(Function<GameTile, Boolean> p) {
        List<TileAndIndex> result = new ArrayList<>();
        for (int i = 0; i < this.tiles.size(); i++) {
            if (p.apply(this.tiles.get(i))) {
                result.add(new TileAndIndex(this.tiles.get(i), i));
            }
        }

        return result;
    }

    /**This method is used get the tiles within a property set
     * @param propertySet the set of tiles
     */
    public List<GameTile> getPropertiesUnderSet(PropertySet propertySet) {
        return this.getPropertiesFilter(TileFilter.setFilter(propertySet));
    }

    /**This method is used to update the number of houses on the board
     * @param house the number of houses added to or removed from the board
     */
    public void updateHouse(int house) {
        if (placeableHouses < totalHouses && placeableHouses >= 0) {
            placeableHouses += house;
        }
    }

    /**This method is used to update the number of hotels on the board
     * @param hotel the number of hotels added to or removed from the board
     */
    public void updateHotel(int hotel) {
        if (placeableHotels < totalHotels && placeableHotels >= 0) {
            placeableHotels += hotel;
        }
    }

    /**This method is used to get the number of houses still available for building
     */
    public int housesAvailable() {
        return placeableHouses;
    }

    /**This method is used to get the number of houses still available for building
     */
    public int hotelsAvailable() {
        return placeableHotels;
    }

    /**This method gets the player in jail to pay the jail release fee
     * @param player This provides the player currently paying to get out of jail
     */
    public void payJailFine(Player player) {
        if (this.isPlayerInJail(player)) {
            this.jailTile.unjailPlayer(player);
            player.changeBalance(-1 * (JailTile.JAIL_FINE));
            gameInterface.notifyPlayerLeftJail(player);
        }
    }

    /**This method jails a player
     * @param player This provides the player currently going to jail
     */
    public void jailPlayer(Player player) {
        if (!this.isPlayerInJail(player)) {
            this.jailTile.jailPlayer(player);
            player.setTilePosition(this.jailIndex);
            gameInterface.notifyPlayerSentToJail(player);
        }
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
        if (this.isPlayerInJail(player)) {
            jailTile.incrementPlayerRolls(player);
            if (jailTile.hasPlayerRolledOutOfJail(player)) {
                this.payJailFine(player);
            } else {
                gameInterface.notifyPlayerStayJail(player);
            }
        }
    }

    /**This method handles a roll that frees a jailed player immediately
     * @param player This provides the player in question
     */
    public void handleSuccessfulJailedPlayerRoll(Player player) {
        if (this.isPlayerInJail(player)) {
            gameInterface.notifyPlayerLeftJail(player);
            jailTile.unjailPlayer(player);
        }
    }

    public List<GameTileDrawable> getTileDrawables() {
        return IntStream.range(0, this.tiles.size())
                .mapToObj(i -> new GameTileDrawable(this.tiles.get(i), i)).collect(Collectors.toList());
    }

    public List<GameTile> getTileNeighbourhood(int tilePosition, int neighbourhoodRadius) {
        List<GameTile> neighbourhood = new ArrayList<>();
        for (int delta = -neighbourhoodRadius / 2; delta < neighbourhoodRadius / 2; delta++) {
            if (tilePosition + delta < 0) {
                int tileIndex = Math.max(0, this.tiles.size() + delta);
                neighbourhood.add(this.tiles.get(tileIndex));
            } else if (tilePosition + delta <= this.tiles.size()) {
                neighbourhood.add(this.tiles.get(tilePosition % this.tiles.size()));
            } else {
                neighbourhood.add(this.tiles.get(tilePosition));
            }
        }

        return neighbourhood;
    }

    public int getPlayerNetWorth(Player player) {
        List<BuyableTile> properties = this.getTilesOwnedByPlayer(player);

        return properties.stream().mapToInt(BuyableTile::getBuyCost).reduce(0, Integer::sum);
    }

}
