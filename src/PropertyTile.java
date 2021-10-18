import org.w3c.dom.Text;

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

    private void onLandOccupied(Player player, Player owner) {
        if (player.getBalance() < this.calculateRent()) {
            player.changeBalance(-1 * player.getBalance());
            owner.changeBalance(player.getBalance());
            this.gameInterface.notifyRentPayment(owner, player, player.getBalance());
            this.gameInterface.notifyBankruptcy(player);
        } else {
            owner.changeBalance(this.calculateRent());
            player.changeBalance(-1 * this.calculateRent());
            this.gameInterface.notifyRentPayment(owner, player, this.calculateRent());
        }
    }

    private void onLandUnoccupied(Player player) {

    }

    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {
        if (this.owner.isPresent()) {
            onLandOccupied(player, this.owner.get());
        } else {
            onLandUnoccupied(player);
        }
    }

    @Override
    public String tileDescription() {
        String desc = "Name: " + this.name +
                "\nProperty Set: " + this.propertySet.name() +
                "\nCost: $" + this.cost +
                "\nRent: $" + this.calculateRent();
        if (this.owner.isPresent()) {
            desc += "\nOwned by: Player" + owner.get().getPlayerID();
        }

        return desc;
    }
}
