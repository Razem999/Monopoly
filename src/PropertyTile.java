import java.util.Optional;

public class PropertyTile implements GameTileI {
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
    private final GameInterfaceI gameInterface;
    private Optional<Player> owner;

    PropertyTile(String name, PropertySet propertySet, GameInterfaceI gameInterface, int cost, int pricePerHouse, int baseRent, int rent1h, int rent2h, int rent3h, int rent4h, int rentHotel) {
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
    }

    private int calculateRent() {
        return this.baseRent;
    }

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

    @Override
    public boolean tryCloseAuctionFor(int price, Player player) {
        player.changeBalance(-1 * price);
        this.owner = Optional.of(player);
        gameInterface.notifyPlayerPurchaseConfirm(player, this.name, price);

        return true;
    }

    @Override
    public boolean isAuctionable() {
        return true;
    }
}
