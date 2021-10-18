public class GoTile implements GameTileI {
    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {
        player.changeBalance(200);
    }

    @Override
    public String tileDescription() {
        return "GO Tile: Gain $200 when passing through.";
    }
}
