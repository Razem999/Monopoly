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
    private Optional<Player> owner;

    PropertyTile(String name, PropertySet propertySet, int cost, int pricePerHouse, int baseRent, int rent1h, int rent2h, int rent3h, int rent4h, int rentHotel) {
        this.name = name;
        this.propertySet = propertySet;
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

    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {

    }

    @Override
    public String tileDescription() {
        String desc = this.name + ": In Property Set " +
                this.propertySet.name() + " it costs $" + this.cost + " and has a rent of $" +
                this.calculateRent();
        if (this.owner.isPresent()) {
            desc += ". It is owned by Player" + owner.get().getPlayerID();
        }

        return desc;
    }
}
