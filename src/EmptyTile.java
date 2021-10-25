/**
 * The EmptyTle represents the community and chance tiles from monopoly without any functionality
 * to diversify the board so its not just property tiles.
 */
public class EmptyTile implements GameTileI{

    //Overrides function getName in GameTile interface and returns the name of the tile
    @Override
    public String getName() {
        return "Empty Tile";
    }

    //Overrides function isOwnedBy in GameTile interface and returns false since this tile cannot be owned
    @Override
    public boolean isOwnedBy(Player player) {
        return false;
    }

    //Overrides function tryTransferOwnership in GameTile interface and returns false since this tile cannot be owned or transferred
    @Override
    public boolean tryTransferOwnership(Player newOwner) {
        return false;
    }

    //Overrides function tryBuy in GameTile interface and returns false since this tile cannot be bought
    @Override
    public boolean tryBuy(Player player){
        return false;
    }

    //Overrides function onLand in GameTile interface and does nothing since the tile does not trigger an event
    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players){
    }

    //Overrides function tileDescription in GameTile interface and returns a String of the Empty Tile's description
    @Override
    public String tileDescription() {
        return "Name: Empty Tile\nDescription: No event is linked with this tile.";
    }

    //Overrides function tryClosedAuctionFor in GameTile interface and returns false since this tile cannot be auctioned
    @Override
    public boolean tryCloseAuctionFor(int price, Player player) {
        return false;
    }

    //Overrides function isAuctionable in GameTile interface and returns false since this tile cannot be auctioned
    @Override
    public boolean isAuctionable() {
        return false;
    }
}
