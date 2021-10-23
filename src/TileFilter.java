public interface TileFilter {
    static TileFilter utilityFilter() {
        return new TileFilter() {
            @Override
            public boolean filter(GameTileI tile) {
                return tile instanceof UtilityTile;
            }
        };
    }
    static TileFilter railroadFilter() {
        return new TileFilter() {
            @Override
            public boolean filter(GameTileI tile) {
                return tile instanceof Railroad;
            }
        };
    }
    boolean filter(GameTileI tile);
}
