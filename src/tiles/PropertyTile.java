package tiles;

import gameLogic.Player;
import gameLogic.Players;
import gameLogic.GameBoard;
import gameInterface.GameInterface;

import java.util.Optional;

/**
 * The tiles.PropertyTile class represents a property tile present on the monopoly board
 *
 * @version 1.0
 * @since 2021-10-25
 */
public class PropertyTile implements HousingTile {
    private final PropertySet propertySet;
    private final String name;
    private final int cost;
    private final int pricePerHouse;
    private final int baseRent;
    private final int rent1h;
    private final int rent2h;
    private final int rent3h;
    private final int rent4h;
    private final int rentHotel;
    private final GameInterface gameInterface;
    private Optional<Player> owner;
    private int houses;
    private int hotels;
    private boolean hasHotel;

    /**This is the constructor of tiles.PropertyTile with parameters
     * @param name This provides the name of the utility tile
     * @param propertySet This provides colour set that the property is a part of
     * @param gameInterface This provides text for each action the player takes
     * @param cost This provides the base cost of the property tile
     * @param pricePerHouse This is the cost of adding one house to the property tile
     * @param baseRent This is the rent of the property tile with no houses
     * @param rent1h This is the rent of the property tile with 1 house
     * @param rent2h This is the rent of the property tile with 2 houses
     * @param rent3h This is the rent of the property tile with 3 houses
     * @param rent4h This is the rent of the property tile with 4 houses
     * @param rentHotel This is the rent of the property tile with a hotel
     */
    public PropertyTile(String name, PropertySet propertySet, GameInterface gameInterface, int cost, int pricePerHouse, int baseRent, int rent1h, int rent2h, int rent3h, int rent4h, int rentHotel) {
        this.name = name;
        this.propertySet = propertySet;
        this.gameInterface = gameInterface;
        this.owner = Optional.empty();
        this.cost = cost;
        this.pricePerHouse = pricePerHouse;
        this.baseRent = baseRent;
        this.rent1h = rent1h;
        this.rent2h = rent2h;
        this.rent3h = rent3h;
        this.rent4h = rent4h;
        this.rentHotel = rentHotel;
        this.houses = 0;
        this.hotels = 0;
        this.hasHotel = false;
    }

    /**This function determines the rent of a property tile
     */
    private int calculateRent() {
        if (this.hasHotel) {
            return rentHotel;
        } else if (this.houses == 4) {
            return rent4h;
        } else if(this.houses == 3) {
            return rent3h;
        } else if (this.houses == 2) {
            return rent2h;
        } else if (this.houses == 1) {
            return rent1h;
        } else {
            return baseRent;
        }
    }

    /**This function determines the rent when a player lands on a property tile owned
     * by another player
     *
     * @param player This provides the player who landed on the tile
     * @param owner This provides the owner of the utility tile
     * @param gameBoard This provides the gameboard with all the tiles
     * @param players This provides a list of all player in the game
     */
    private void onLandOccupied(Player player, Player owner, GameBoard gameBoard, Players players) {
        if (player.equals(owner)) {
            this.gameInterface.notifyPlayerOwnsThis(player);
        }
        else if (player.getBalance() < this.calculateRent()) {
            player.changeBalance(-1 * player.getBalance());
            owner.changeBalance(player.getBalance());
            this.gameInterface.notifyRentPayment(owner, player, player.getBalance());
            this.gameInterface.notifyBankruptcy(player);

            gameBoard.transferPlayerProperties(player, owner);
            players.removePlayer(player);
        } else {
            owner.changeBalance(this.calculateRent());
            player.changeBalance(-1 * this.calculateRent());
            this.gameInterface.notifyRentPayment(owner, player, this.calculateRent());
        }
    }

    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {
        this.owner.ifPresent(value -> onLandOccupied(player, value, gameBoard, players));
    }

    @Override
    public String tileDescription() {
        String desc = "Name: " + this.name +
                "\nProperty Set: " + this.propertySet.name() +
                "\nCost: $" + this.cost +
                "\nRent: $" + this.calculateRent();
        if (this.owner.isPresent()) {
            desc += "\nOwned by: Player" + owner.get().getPlayerID();
        } else {
            desc += "\nCan Be Bought";
        }

        return desc;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public PropertySet getPropertySet() {
        return propertySet;
    }

    @Override
    public PropertyTile getPropertyTile() {
        return this;
    }

    @Override
    public void buy(Player player) {
        if (this.owner.isPresent()) {
            gameInterface.notifyCannotBuyAlreadyOwned(player, this.owner.get(), this);
            return;
        }

        if (player.getBalance() < this.cost) {
            gameInterface.notifyCannotBuyTileBalanceReasons(player, this);
        } else {
            boolean choice = gameInterface.processSale(this.name, this.cost, player);
            if (choice) {
                player.changeBalance(-1 * cost);
                this.owner = Optional.of(player);
                gameInterface.notifyPlayerPurchaseConfirm(player, this.name, this.cost);
            } else {
                gameInterface.notifyPlayerDeclinedPurchase(player, this.name);
            }
        }
    }

    private void buyHouse(Player player, GameBoard gameBoard) {
        if (player.getBalance() < this.pricePerHouse) {
            gameInterface.notifyCannotBuyHouseBalanceReasons(player, this);
        } else if (gameBoard.housesAvailable() == 0) {
            gameInterface.notifyHousesUnavailable(player);
        } else {
            player.changeBalance(-1 * this.pricePerHouse);
            gameBoard.updateHouse(-1);
            gameInterface.notifyPlayerPurchasedHouse(player, this.name, this.pricePerHouse);
            this.houses++;
        }
    }

    private void buyHotel(Player player, GameBoard gameBoard) {
        if (player.getBalance() < this.pricePerHouse) {
            gameInterface.notifyCannotBuyHouseBalanceReasons(player, this);
        } else if (gameBoard.housesAvailable() == 0) {
            gameInterface.notifyHousesUnavailable(player);
        } else {
            player.changeBalance(-1 * this.pricePerHouse);
            gameBoard.updateHotel(-1);
            gameInterface.notifyPlayerPurchasedHouse(player, this.name, this.pricePerHouse);
            this.hotels++;
        }
    }

    @Override
    public void upgradeProperty(Player player, GameBoard gameBoard) {
        if (owner.map(o -> o.equals(player)).orElse(false)) {
            if (this.houses < 4) {
                this.buyHouse(player, gameBoard);
            } else if (this.houses == 4 && !this.hasHotel) {
                this.buyHotel(player, gameBoard);
            } else {
                gameInterface.notifyTileCannotUpgradeFurther(player, this);
            }
        } else {
            gameInterface.notifyCannotBuyHouseOwnershipReasons(player, this.owner, this);
        }
    }

    @Override
    public int numberOfHouses() {
        return this.houses;
    }

    @Override
    public void transferOwnership(Player newOwner) {
        this.owner = Optional.of(newOwner);
    }

    @Override
    public void closeAuctionFor(int price, Player player) {
        player.changeBalance(-1 * price);
        this.owner = Optional.of(player);
        gameInterface.notifyPlayerPurchaseConfirm(player, this.name, price);
    }

    @Override
    public boolean isOwnedBy(Player player) {
        return this.owner.map(value -> value.equals(player)).orElse(false);

    }

    @Override
    public boolean hasOwner() {
        return this.owner.isPresent();
    }

    @Override
    public int getBuyCost() {
        return cost;
    }

    @Override
    public Optional<HousingTile> asHousingTile() {
        return Optional.of(this);
    }

    @Override
    public Optional<BuyableTile> asBuyable() {
        return Optional.of(this);
    }

}
