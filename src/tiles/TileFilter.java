package tiles;

public interface TileFilter {
    static TileFilter utilityFilter() {
        return tile -> tile instanceof UtilityTile;
    }
    static TileFilter railroadFilter() {
        return tile -> tile instanceof RailroadTile;
    }
    static TileFilter setFilter(PropertySet propertySet) {
        return tile -> tile instanceof PropertyTile;
    }
    boolean filter(GameTile tile);
}
