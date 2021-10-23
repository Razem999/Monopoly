public class EmptyTile implements GameTileI{

    @Override
    public String getName() {
        return "Empty Tile";
    }

    @Override
    public boolean isOwnedBy(Player player) {
        return false;
    }

    @Override
    public boolean tryTransferOwnership(Player newOwner) {
        return false;
    }

    @Override
    public boolean tryBuy(Player player){
        return false;
    }

    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players){
    }

    @Override
    public String tileDescription() {
        return "Name: Empty Tile\nDescription: No event is linked with this tile.";
    }
}
