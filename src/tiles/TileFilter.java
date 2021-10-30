package tiles;

public interface TileFilter {
    static TileFilter utilityFilter() {
        return tile -> tile instanceof UtilityTile;
    }
    static TileFilter railroadFilter() {
        return tile -> tile instanceof RailroadTile;
    }
    boolean filter(GameTileI tile);
}
