import java.util.LinkedList;
import java.util.List;

public class PropertyTileBuilder {
    private final PropertySet propertySet;
    private final String name;
    private final GameInterfaceI textGameInterface;
    private int cost;
    private int pricePerHouse;
    private int baseRent;
    private int rent1h;
    private int rent2h;
    private int rent3h;
    private int rent4h;
    private int rentHotel;

    PropertyTileBuilder(String name, PropertySet propertySet, GameInterfaceI textGameInterface) {
        this.propertySet = propertySet;
        this.name = name;
        this.textGameInterface = textGameInterface;
    }

    public PropertyTileBuilder setCost(int cost) {
        this.cost = cost;
        return this;
    }

    public PropertyTileBuilder setPricePerHouse(int pricePerHouse) {
        this.pricePerHouse = pricePerHouse;
        return this;
    }

    public PropertyTileBuilder setRent(int baseRent, int rent1h, int rent2h, int rent3h, int rent4h, int rentHotel) {
        this.baseRent = baseRent;
        this.rent1h = rent1h;
        this.rent2h = rent2h;
        this.rent3h = rent3h;
        this.rent4h = rent4h;
        this.rentHotel = rentHotel;
        return this;
    }

    public PropertyTile getPropertyTile() {
        return new PropertyTile(name, propertySet, textGameInterface, cost, pricePerHouse, baseRent, rent1h, rent2h, rent3h, rent4h, rentHotel);
    }

    public static List<PropertyTile> createTiles(GameInterfaceI gameInterface) {
        List<PropertyTile> properties = new LinkedList<>();

        //Mediterranean Avenue
        properties.add(new PropertyTileBuilder("Mediterranean Avenue", PropertySet.Purple, gameInterface).setCost(60).setPricePerHouse(50).setRent(2, 10, 30, 90, 160, 250).getPropertyTile());

        //Baltic Avenue
        properties.add(new PropertyTileBuilder("Baltic Avenue", PropertySet.Purple, gameInterface).setCost(60).setPricePerHouse(50).setRent(4, 20, 60, 180, 320, 450).getPropertyTile());

        //Oriental Avenue
        properties.add(new PropertyTileBuilder("Oriental Avenue", PropertySet.LightBlue, gameInterface).setCost(100).setPricePerHouse(50).setRent(6, 30, 90, 270, 400, 550).getPropertyTile());

        //Vermont Avenue
        properties.add(new PropertyTileBuilder("Vermont Avenue", PropertySet.LightBlue, gameInterface).setCost(100).setPricePerHouse(50).setRent(6, 30, 90, 270, 400, 550).getPropertyTile());

        //Connecticut Avenue
        properties.add(new PropertyTileBuilder("Connecticut Avenue", PropertySet.LightBlue, gameInterface).setCost(120).setPricePerHouse(50).setRent(8, 40, 100, 300, 450, 600).getPropertyTile());

        //St. Charles Place
        properties.add(new PropertyTileBuilder("St. Charles Place", PropertySet.Pink, gameInterface).setCost(140).setPricePerHouse(100).setRent(10, 50, 150, 450, 625, 750).getPropertyTile());

        //States Avenue
        properties.add(new PropertyTileBuilder("States Avenue", PropertySet.Pink, gameInterface).setCost(140).setPricePerHouse(100).setRent(10, 50, 150, 450, 625, 750).getPropertyTile());

        //Virginia Avenue
        properties.add(new PropertyTileBuilder("Virginia Avenue", PropertySet.Pink, gameInterface).setCost(160).setPricePerHouse(100).setRent(12, 60, 180, 500, 700, 900).getPropertyTile());

        //St. James Place
        properties.add(new PropertyTileBuilder("St. James Place", PropertySet.Orange, gameInterface).setCost(180).setPricePerHouse(100).setRent(14, 70, 200, 550, 750, 950).getPropertyTile());

        //Tennessee Avenue
        properties.add(new PropertyTileBuilder("Tennessee Avenue", PropertySet.Orange, gameInterface).setCost(180).setPricePerHouse(100).setRent(14, 70, 200, 550, 750, 950).getPropertyTile());

        //New York Avenue
        properties.add(new PropertyTileBuilder("New York Avenue", PropertySet.Orange, gameInterface).setCost(200).setPricePerHouse(100).setRent(16, 80, 220, 600, 800, 1000).getPropertyTile());

        //Kentucky Avenue
        properties.add(new PropertyTileBuilder("Kentucky Avenue", PropertySet.Red, gameInterface).setCost(220).setPricePerHouse(150).setRent(18, 90, 250, 700, 875, 1050).getPropertyTile());

        //Indiana Avenue
        properties.add(new PropertyTileBuilder("Indiana Avenue", PropertySet.Red, gameInterface).setCost(220).setPricePerHouse(150).setRent(18, 90, 250, 700, 875, 1050).getPropertyTile());

        //Illinois Avenue
        properties.add(new PropertyTileBuilder("Illinois Avenue", PropertySet.Red, gameInterface).setCost(240).setPricePerHouse(150).setRent(20, 100, 300, 750, 925, 1100).getPropertyTile());

        //Atlantic Avenue
        properties.add(new PropertyTileBuilder("Atlantic Avenue", PropertySet.Yellow, gameInterface).setCost(260).setPricePerHouse(150).setRent(22, 110, 330, 800, 975, 1150).getPropertyTile());

        //Ventnor Avenue
        properties.add(new PropertyTileBuilder("Ventnor Avenue", PropertySet.Yellow, gameInterface).setCost(260).setPricePerHouse(150).setRent(22, 110, 330, 800, 975, 1150).getPropertyTile());

        //Marvin Gardens
        properties.add(new PropertyTileBuilder("Marvin Gardens", PropertySet.Yellow, gameInterface).setCost(280).setPricePerHouse(150).setRent(24, 120, 360, 850, 1025, 1200).getPropertyTile());

        //Pacific Avenue
        properties.add(new PropertyTileBuilder("Pacific Avenue", PropertySet.Green, gameInterface).setCost(300).setPricePerHouse(200).setRent(26, 130, 390, 900, 1100, 1275).getPropertyTile());

        //North Carolina Avenue
        properties.add(new PropertyTileBuilder("North Carolina Avenue", PropertySet.Green, gameInterface).setCost(300).setPricePerHouse(200).setRent(26, 130, 390, 900, 1100, 1275).getPropertyTile());

        //Pennsylvania Avenue
        properties.add(new PropertyTileBuilder("Pennsylvania Avenue", PropertySet.Green, gameInterface).setCost(320).setPricePerHouse(200).setRent(28, 150, 450, 1000, 1200, 1400).getPropertyTile());

        //Park Place
        properties.add(new PropertyTileBuilder("Park Place", PropertySet.Blue, gameInterface).setCost(350).setPricePerHouse(200).setRent(35, 175, 500, 1100, 1300, 1500).getPropertyTile());

        //Boardwalk
        properties.add(new PropertyTileBuilder("Boardwalk", PropertySet.Blue, gameInterface).setCost(400).setPricePerHouse(200).setRent(50, 200, 600, 1400, 1700, 2000).getPropertyTile());

        return properties;
    }
}
