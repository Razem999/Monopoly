public class GoTile implements GameTileI {
    public static int PassReward = 200;

    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {
        player.changeBalance(200);
    }

    @Override
    public String tileDescription() {
        return "Name: GO Tile\nDescription: Gain $200 when passing through.";
    }
}
