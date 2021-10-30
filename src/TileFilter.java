public interface TileFilter {
    static TileFilter utilityFilter() {
        return tile -> tile instanceof UtilityTile;
    }
    static TileFilter railroadFilter() {
        return tile -> tile instanceof Railroad;
    }
    boolean filter(GameTileI tile);
}
