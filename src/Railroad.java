import java.util.Optional;

public class Railroad implements GameTileI{
    private final PropertySet propertySet;
    private final String name;
    private final int cost;
    private final int baseRent;
    private final int rent2r;
    private final int rent3r;
    private final int rent4r;
    private int totalOwned;
    private final GameInterfaceI gameInterface;
    private Optional<Player> owner;

    public Railroad(String name, PropertySet propertySet, GameInterfaceI gameInterface, int cost, int baseRent, int rent2r, int rent3r, int rent4r) {
        this.name = name;
        this.propertySet = propertySet;
        this.gameInterface = gameInterface;
        this.owner = Optional.empty();
        this.cost = cost;
        this.baseRent = baseRent;
        this.rent2r = rent2r;
        this.rent3r = rent3r;
        this.rent4r = rent4r;
    }

    private int calculateRent() {
        return this.baseRent;
    }

    private void onLandOccupied(Player player, Player owner, GameBoard gameBoard, Players players) {
        if (player.equals(owner)) {
            this.gameInterface.notifyYouOwnThis(player);
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

//    private void addRailroadOwned(Player player, Railroad rr) {
//
//    }
//
//
//    private int totalRailroadsOwned() {
//        return totalOwned;
//    }

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
            desc += "\nOwned by: Player" + owner.get().getPlayerID() +
                "\nRailroads owned: " + this.totalOwned;

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
