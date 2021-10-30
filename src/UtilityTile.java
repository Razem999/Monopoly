import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The UtilityTiles class represents the Utility tiles from the original
 * monopoly gameboard. (Water works and Electric company)
 *
 * @version 1.0
 * @since 2021-10-25
 */
public class UtilityTile implements BuyableI {

    private final String name;
    private final int cost;
    private final GameInterfaceI gameInterface;
    private Optional<Player> owner;


    /**This is the constructor of GameBoard with a parameter
     * @param name This provides the name of the utility tile
     * @param cost This provides the base cost of the utility tile
     * @param gameInterface This provides text for each action the player takes
     */
    UtilityTile(String name, int cost, GameInterfaceI gameInterface) {
        this.name = name;
        this.cost = cost;
        this.gameInterface = gameInterface;
        this.owner = Optional.empty();
    }

    /**This function determines the rent when a player lands on a utility tile owned
     * by another player
     *
     * @param player This provides the player who landed on the tile
     * @param owner This provides the owner of the utility tile
     * @param gameBoard This provides the gameboard with all the tiles
     */
    public void onLandOccupied(Player player, Player owner, GameBoard gameBoard) {
        int firstDie = ThreadLocalRandom.current().nextInt(1, 6 + 1);
        int secondDie = ThreadLocalRandom.current().nextInt(1, 6 + 1);
        if (gameBoard.getPropertiesFilter(TileFilter.utilityFilter()).size() == 2) {
            player.changeBalance(10 * (firstDie + secondDie));
        } else {
            player.changeBalance(4 * (firstDie + secondDie));
        }
        this.gameInterface.notifyRentPayment(owner, player, player.getBalance());
        this.gameInterface.notifyBankruptcy(player);
    }

    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {
        this.owner.ifPresent(value -> onLandOccupied(player, value, gameBoard));
    }

    @Override
    public String tileDescription() {
        String desc = "Name: " + this.name + "\nA utility tile";
        if (this.owner.isPresent()) {
            desc += "\nOwned by: Player" + owner.get().getPlayerID();
        } else {
            desc += "\nCan Be Bought";
        }
        return desc;
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
    public String getName() {
        return this.name;
    }

    @Override
    public Optional<BuyableI> asBuyable() {
        return Optional.empty();
    }

    @Override
    public boolean isOwnedBy(Player player) {
        return this.owner.map(value -> value.equals(player)).orElse(false);
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

}
