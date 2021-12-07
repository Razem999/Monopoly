package tiles;

import gameInterface.GameInterface;
import gameInterface.GameVersionConfiguration;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The tiles.PropertyTile class provide functions for making property tiles. Simplifies the process.
 *
 * @version 1.0
 * @since 2021-10-25
 */
public class PropertyTileBuilder {
    private final PropertySet propertySet;
    private final String name;
    private final GameInterface gameInterface;
    private int cost;
    private int pricePerHouse;
    private int baseRent;
    private int rent1h;
    private int rent2h;
    private int rent3h;
    private int rent4h;
    private int rentHotel;

    /**This is the constructor of tiles.PropertyTileBuilder with parameters
     * @param name This provides the name of the utility tile
     * @param propertySet This provides colour set that the property is a part of
     * @param textGameInterface This provides text for each action the player takes
     */
    PropertyTileBuilder(String name, PropertySet propertySet, GameInterface textGameInterface) {
        this.propertySet = propertySet;
        this.name = name;
        this.gameInterface = textGameInterface;
    }

    /**This function sets the cost of a property tile
     *
     * @param cost This provides the cost of the property tile
     */
    public PropertyTileBuilder setCost(int cost) {
        this.cost = cost;
        return this;
    }

    /**This function sets the price to build a house on the property tile
     *
     * @param pricePerHouse This provides the cost of a house
     */
    public PropertyTileBuilder setPricePerHouse(int pricePerHouse) {
        this.pricePerHouse = pricePerHouse;
        return this;
    }

    /**This function sets the rent of the property tile in all its states
     *
     * @param baseRent This provides the rent of the property tile with no houses
     * @param rent1h This provides the rent of the property tile with 1 house
     * @param rent2h This provides the rent of the property tile with 2 houses
     * @param rent3h This provides the rent of the property tile with 3 houses
     * @param rent4h This provides the rent of the property tile with 4 houses
     * @param rentHotel This provides the rent of the property tile with a hotel
     */
    public PropertyTileBuilder setRent(int baseRent, int rent1h, int rent2h, int rent3h, int rent4h, int rentHotel) {
        this.baseRent = baseRent;
        this.rent1h = rent1h;
        this.rent2h = rent2h;
        this.rent3h = rent3h;
        this.rent4h = rent4h;
        this.rentHotel = rentHotel;
        return this;
    }

    /**This function returns the property tile with all its information set
     */
    public PropertyTile getPropertyTile() {
        return new PropertyTile(name, propertySet, gameInterface, cost, pricePerHouse, baseRent, rent1h, rent2h, rent3h, rent4h, rentHotel);
    }

    /**This function creates and returns all the tiles from the monopoly game
     * @param gameInterface This provides text for each action the player takes
     */
    public static List<PropertyTile> createTiles(GameInterface gameInterface) {

        Map<String, String> streetNames = GameVersionConfiguration.getInstance().getStreetNames();
        List<PropertyTile> properties = new LinkedList<>();

        //Mediterranean Avenue
        properties.add(new PropertyTileBuilder(streetNames.get("mediterraneanAvenue"), PropertySet.Purple, gameInterface).setCost(60).setPricePerHouse(50).setRent(2, 10, 30, 90, 160, 250).getPropertyTile());

        //Baltic Avenue
        properties.add(new PropertyTileBuilder(streetNames.get("balticAvenue"), PropertySet.Purple, gameInterface).setCost(60).setPricePerHouse(50).setRent(4, 20, 60, 180, 320, 450).getPropertyTile());

        //Oriental Avenue
        properties.add(new PropertyTileBuilder(streetNames.get("orientalAvenue"), PropertySet.LightBlue, gameInterface).setCost(100).setPricePerHouse(50).setRent(6, 30, 90, 270, 400, 550).getPropertyTile());

        //Vermont Avenue
        properties.add(new PropertyTileBuilder(streetNames.get("vermontAvenue"), PropertySet.LightBlue, gameInterface).setCost(100).setPricePerHouse(50).setRent(6, 30, 90, 270, 400, 550).getPropertyTile());

        //Connecticut Avenue
        properties.add(new PropertyTileBuilder(streetNames.get("connecticutAvenue"), PropertySet.LightBlue, gameInterface).setCost(120).setPricePerHouse(50).setRent(8, 40, 100, 300, 450, 600).getPropertyTile());

        //St. Charles Place
        properties.add(new PropertyTileBuilder(streetNames.get("stCharlesPlace"), PropertySet.Pink, gameInterface).setCost(140).setPricePerHouse(100).setRent(10, 50, 150, 450, 625, 750).getPropertyTile());

        //States Avenue
        properties.add(new PropertyTileBuilder(streetNames.get("statesAvenue"), PropertySet.Pink, gameInterface).setCost(140).setPricePerHouse(100).setRent(10, 50, 150, 450, 625, 750).getPropertyTile());

        //Virginia Avenue
        properties.add(new PropertyTileBuilder(streetNames.get("virginiaAvenue"), PropertySet.Pink, gameInterface).setCost(160).setPricePerHouse(100).setRent(12, 60, 180, 500, 700, 900).getPropertyTile());

        //St. James Place
        properties.add(new PropertyTileBuilder(streetNames.get("stJamesPlace"), PropertySet.Orange, gameInterface).setCost(180).setPricePerHouse(100).setRent(14, 70, 200, 550, 750, 950).getPropertyTile());

        //Tennessee Avenue
        properties.add(new PropertyTileBuilder(streetNames.get("tennesseeAvenue"), PropertySet.Orange, gameInterface).setCost(180).setPricePerHouse(100).setRent(14, 70, 200, 550, 750, 950).getPropertyTile());

        //New York Avenue
        properties.add(new PropertyTileBuilder(streetNames.get("newYorkAvenue"), PropertySet.Orange, gameInterface).setCost(200).setPricePerHouse(100).setRent(16, 80, 220, 600, 800, 1000).getPropertyTile());

        //Kentucky Avenue
        properties.add(new PropertyTileBuilder(streetNames.get("kentuckyAvenue"), PropertySet.Red, gameInterface).setCost(220).setPricePerHouse(150).setRent(18, 90, 250, 700, 875, 1050).getPropertyTile());

        //Indiana Avenue
        properties.add(new PropertyTileBuilder(streetNames.get("indianaAvenue"), PropertySet.Red, gameInterface).setCost(220).setPricePerHouse(150).setRent(18, 90, 250, 700, 875, 1050).getPropertyTile());

        //Illinois Avenue
        properties.add(new PropertyTileBuilder(streetNames.get("illinoisAvenue"), PropertySet.Red, gameInterface).setCost(240).setPricePerHouse(150).setRent(20, 100, 300, 750, 925, 1100).getPropertyTile());

        //Atlantic Avenue
        properties.add(new PropertyTileBuilder(streetNames.get("atlanticAvenue"), PropertySet.Yellow, gameInterface).setCost(260).setPricePerHouse(150).setRent(22, 110, 330, 800, 975, 1150).getPropertyTile());

        //Ventnor Avenue
        properties.add(new PropertyTileBuilder(streetNames.get("ventnorAvenue"), PropertySet.Yellow, gameInterface).setCost(260).setPricePerHouse(150).setRent(22, 110, 330, 800, 975, 1150).getPropertyTile());

        //Marvin Gardens
        properties.add(new PropertyTileBuilder(streetNames.get("marvinGardens"), PropertySet.Yellow, gameInterface).setCost(280).setPricePerHouse(150).setRent(24, 120, 360, 850, 1025, 1200).getPropertyTile());

        //Pacific Avenue
        properties.add(new PropertyTileBuilder(streetNames.get("pacificAvenue"), PropertySet.Green, gameInterface).setCost(300).setPricePerHouse(200).setRent(26, 130, 390, 900, 1100, 1275).getPropertyTile());

        //North Carolina Avenue
        properties.add(new PropertyTileBuilder(streetNames.get("northCarolinaAvenue"), PropertySet.Green, gameInterface).setCost(300).setPricePerHouse(200).setRent(26, 130, 390, 900, 1100, 1275).getPropertyTile());

        //Pennsylvania Avenue
        properties.add(new PropertyTileBuilder(streetNames.get("pennsylvaniaAvenue"), PropertySet.Green, gameInterface).setCost(320).setPricePerHouse(200).setRent(28, 150, 450, 1000, 1200, 1400).getPropertyTile());

        //Park Place
        properties.add(new PropertyTileBuilder(streetNames.get("parkPlace"), PropertySet.Blue, gameInterface).setCost(350).setPricePerHouse(200).setRent(35, 175, 500, 1100, 1300, 1500).getPropertyTile());

        //Boardwalk
        properties.add(new PropertyTileBuilder(streetNames.get("boardwalk"), PropertySet.Blue, gameInterface).setCost(400).setPricePerHouse(200).setRent(50, 200, 600, 1400, 1700, 2000).getPropertyTile());

        return properties;
    }
}
