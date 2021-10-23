import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class UtilityTile implements GameTileI {

    private final String name;
    private final int cost;
    private final GameInterfaceI gameInterface;
    private Optional<Player> owner;


    UtilityTile(String name, int cost, GameInterfaceI gameInterface) {
        this.name = name;
        this.cost = cost;
        this.gameInterface = gameInterface;
        this.owner = Optional.empty();
    }

    public void onLandOccupied(Player player, Player owner, GameBoard gameBoard, Players players) {
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
        this.owner.ifPresent(value -> onLandOccupied(player, value, gameBoard, players));
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
    public boolean tryBuy(Player player) {
        if (this.owner.isPresent()) {
            gameInterface.notifyCannotBuyAlreadyOwned(player, this.owner.get(), this);
            return false;
        }
        if (player.getBalance() < this.cost) {
            gameInterface.notifyCannotBuyTileBalanceReasons(player, this);
            return false;
        } else {
            boolean choice = gameInterface.processSale(this.name, this.cost, player);
            if (choice) {
                player.changeBalance(-1 * cost);
                this.owner = Optional.of(player);
                gameInterface.notifyPlayerPurchaseConfirm(player, this.name, this.cost);

                return true;
            } else {
                gameInterface.notifyPlayerDeclinedPurchase(player, this.name);

                return false;
            }
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isOwnedBy(Player player) {
        return this.owner.map(value -> value.equals(player)).orElse(false);
    }

    @Override
    public boolean tryTransferOwnership(Player newOwner) {
        this.owner = Optional.of(newOwner);
        return true;
    }
}
