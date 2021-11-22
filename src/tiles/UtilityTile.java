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

    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {
        this.playerOwner.ifPresent(value -> onLandOccupied(player, value, gameBoard, players));
    }

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

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public PropertySet getPropertySet() {
        return PropertySet.White;
    }

    @Override
    public PropertyTile getPropertyTile() {
        return null;
    }

    @Override
    public Optional<BuyableTile> asBuyable() {
        return Optional.of(this);
    }

    @Override
    public Optional<HousingTile> asHousingTile() {
        return Optional.empty();
    }

    @Override
    public boolean isOwnedBy(Player player) {
        return this.playerOwner.map(value -> value.equals(player)).orElse(false);
    }

    @Override
    public boolean hasOwner() {
        return this.playerOwner.isPresent();
    }

    @Override
    public int getBuyCost() {
        return this.cost;
    }

    @Override
    public void transferOwnership(Player newOwner) {
        this.playerOwner = Optional.of(newOwner);
    }

    @Override
    public void closeAuctionFor(int price, Player player) {
        player.changeBalance(-1 * price);
        this.playerOwner = Optional.of(player);
        gameInterface.notifyPlayerPurchaseConfirm(player, this.name, price);
    }

}
