package tiles;

import gameLogic.Player;
import gameLogic.Players;
import gameLogic.GameBoard;
import gameInterface.GameInterfaceI;

import java.util.Optional;

/**
 * The Railroad class represents the railroads tile from the original game,
 * where all the railroads cost the same and the rent to pay is correlated
 * with the number of railroads owned by the gameLogic.Player.
 *
 * @version 1.0
 * @since 2021-10-25
 */
public class RailroadTile implements BuyableI {
    private final String name;
    private final int cost;
    private int totalOwned;
    private final GameInterfaceI gameInterface;
    private Optional<Player> owner;

    /**
     * This is the constructor of Railroad with parameters.
     * The container (owner) is initialized here.
     * @param name This is the name of the railroad
     * @param gameInterface This provides text for each action the player takes
     * @param cost This is the cost of the railroad
     */
    public RailroadTile(String name, GameInterfaceI gameInterface, int cost) {
        this.name = name;
        this.gameInterface = gameInterface;
        this.owner = Optional.empty();
        this.cost = cost;
    }

    /**
     * This method is used to check the number of railroads the owner owns, and calculates the rent
     * the player owes to the owner.
     * @return int This returns the rent the player owes to the owner
     */
    private int calculateRent() {
        if(totalOwned == 4) {
            return 200;
        } else if (totalOwned == 3) {
            return 100;
        } else if (totalOwned == 2) {
            return 50;
        } else {
            return 25;
        }
    }

    /**
     * This method is used to identify the player that landed on an occupied tile, and pay rent to
     * the player that owns the tile. It checks to see if the player has enough money to be able to
     * pay the rent. If the player has insufficient funds, the gameLogic.Player paying transfers all his money and
     * properties to the owner of that tile. Otherwise, they transfer the rent amount to the owner.
     * @param player This is the gameLogic.Player who is paying
     * @param owner This is the gameLogic.Player who is receiving the payment
     * @param gameBoard This is the board in which the tile is situated
     * @param players This is a list of players playing the game
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
            this.totalOwned = gameBoard.getPropertiesFilter(TileFilter.utilityFilter()).size();
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
                "\nCost: $" + this.cost +
                "\nRent: $" + this.calculateRent();
        if (this.owner.isPresent()) {
            desc += "\nOwned by: gameLogic.Player" + owner.get().getPlayerID() +
                "\nRailroads owned: " + this.totalOwned;

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
        return PropertySet.White;
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
    public Optional<BuyableI> asBuyable() {
        return Optional.of(this);
    }
}
