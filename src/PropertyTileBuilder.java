public class PropertyTileBuilder {
    private PropertySet propertySet;
    private String name;
    private int cost;
    private int pricePerHouse;
    private int baseRent;
    private int rent1h;
    private int rent2h;
    private int rent3h;
    private int rent4h;
    private int rentHotel;

    PropertyTileBuilder(String name, PropertySet propertySet) {
        this.propertySet = propertySet;
        this.name = name;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setPricePerHouse(int pricePerHouse) {
        this.pricePerHouse = pricePerHouse;
    }

    public void setRent(int baseRent, int rent1h, int rent2h, int rent3h, int rent4h, int rentHotel) {
        this.baseRent = baseRent;
        this.rent1h = rent1h;
        this.rent2h = rent2h;
        this.rent3h = rent3h;
        this.rent4h = rent4h;
        this.rentHotel = rentHotel;
    }

    public PropertyTile getPropertyTile() {
        return new PropertyTile(name, propertySet, cost, pricePerHouse, baseRent, rent1h, rent2h, rent3h, rent4h, rentHotel);
    }
}
