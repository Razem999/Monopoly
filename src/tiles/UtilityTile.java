package tiles;

import gameLogic.Player;
import gameLogic.Players;
import gameLogic.GameBoard;
import gameInterface.GameInterface;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The UtilityTiles class represents the Utility tiles from the original
 * monopoly gameboard. (Water works and Electric company)
 *
 * @version 1.0
 * @since 2021-10-25
 */
public class UtilityTile implements BuyableTile {

    private final String name;
    private final int cost;
    private final GameInterface gameInterface;
    private Optional<Player> playerOwner;


    /**This is the constructor of GameBoard with a parameter
     * @param name This provides the name of the utility tile
     * @param cost This provides the base cost of the utility tile
     * @param gameInterface This provides text for each action the player takes
     */
    public UtilityTile(String name, int cost, GameInterface gameInterface) {
        this.name = name;
        this.cost = cost;
        this.gameInterface = gameInterface;
        this.playerOwner = Optional.empty();
    }

    /**This function determines the rent when a player lands on a utility tile owned
     * by another player
     *
     * @param player This provides the player who landed on the tile
     * @param owner This provides the owner of the utility tile
     * @param gameBoard This provides the gameboard with all the tiles
     */
    public void onLandOccupied(Player player, Player owner, GameBoard gameBoard, Players players) {
        int firstDie = ThreadLocalRandom.current().nextInt(1, 6 + 1);
        int secondDie = ThreadLocalRandom.current().nextInt(1, 6 + 1);
        int rent = 0;
        List<BuyableTile> ownedProperties = gameBoard.getTilesOwnedByPlayer(owner);
        if (ownedProperties.stream()
                .filter(t -> TileFilter.utilityFilter().filter(t)).count() == 2) {
            rent = 10 * (firstDie + secondDie);
        } else {
            rent = 4 * (firstDie + secondDie);
        }
        if (player.getBalance() < rent) {
            player.changeBalance(-1 * player.getBalance());
            owner.changeBalance(player.getBalance());
            this.gameInterface.notifyRentPayment(owner, player, player.getBalance());
            this.gameInterface.notifyBankruptcy(player);

            gameBoard.transferPlayerProperties(player, owner);
            players.removePlayer(player);
        } else {
            player.changeBalance(-1 * rent);
            this.gameInterface.notifyRentPayment(owner, player, rent);
        }
    }


    /**This method is used to determine what is done when a player lands on the utility tile
     * @param player the player that has landed on the utility tile
     * @param players the players in the game
     * @param gameBoard gives information on all tiles
     */
    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {
        this.playerOwner.ifPresent(value -> onLandOccupied(player, value, gameBoard, players));
    }


    /**This method is used to print the description of the current utility tile
     */
    @Override
    public String tileDescription() {
        String desc = "Name: " + this.name + "\nA utility tile";
        if (this.playerOwner.isPresent()) {
            desc += "\nOwned by: Player" + playerOwner.get().getPlayerID();
        } else {
            desc += "\nCan Be Bought";
        }
        return desc;
    }


    /**This method is used when a player tries to buy the current utility tile
     * @param player the player that is trying to buy the utility tile
     */
    @Override
    public void buy(Player player) {
        if (this.playerOwner.isPresent()) {
            gameInterface.notifyCannotBuyAlreadyOwned(player, this.playerOwner.get(), this);
            return;
        }
        if (player.getBalance() < this.cost) {
            gameInterface.notifyCannotBuyTileBalanceReasons(player, this);
        } else {
            boolean choice = gameInterface.processSale(this.name, this.cost, player);
            if (choice) {
                player.changeBalance(-1 * cost);
                this.playerOwner = Optional.of(player);
                gameInterface.notifyPlayerPurchaseConfirm(player, this.name, this.cost);
            } else {
                gameInterface.notifyPlayerDeclinedPurchase(player, this.name);
            }
        }
    }


    /**This method is used to get the name of the utility tile
     */
    @Override
    public String getName() {
        return this.name;
    }


    /**This method is used to get the property set of the utility tile
     */
    @Override
    public PropertySet getPropertySet() {
        return PropertySet.White;
    }


    /**This method is used to get the property tile but returns null since this is not a property tile
     */
    @Override
    public PropertyTile getPropertyTile() {
        return null;
    }


    /**This method is used to get whether this is Utility tile is buyable
     */
    @Override
    public Optional<BuyableTile> asBuyable() {
        return Optional.of(this);
    }


    /**This method is used to get whether the tile is available to place houses on
     */
    @Override
    public Optional<HousingTile> asHousingTile() {
        return Optional.empty();
    }

    /**
     * This method is used to check whether a utility is owned by a specific player or not
     * @param player This is the player in question
     * @return
     */
    @Override
    public boolean isOwnedBy(Player player) {
        return this.playerOwner.map(value -> value.equals(player)).orElse(false);
    }

    /**
     * This method is used to check the utility has a owner or not
     * @return This return true or false depending on the vacancy
     */
    @Override
    public boolean hasOwner() {
        return this.playerOwner.isPresent();
    }

    /**
     * This method is used to check the price of the utility
     * @return This returns the cost of the utility
     */
    @Override
    public int getBuyCost() {
        return this.cost;
    }

    /**
     * This method is used to transfer ownership of a utility to another player
     * @param newOwner This is the player receiving the utility
     */
    @Override
    public void transferOwnership(Player newOwner) {
        this.playerOwner = Optional.of(newOwner);
    }

    /**
     * This method is used to end an auction and have the winning bidder pay for the utility
     * @param price This is the amount the winning bidder has bid
     * @param player This is the player who won the auction
     */
    @Override
    public void closeAuctionFor(int price, Player player) {
        player.changeBalance(-1 * price);
        this.playerOwner = Optional.of(player);
        gameInterface.notifyPlayerPurchaseConfirm(player, this.name, price);
    }

}
