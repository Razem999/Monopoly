public class GoTile implements GameTileI {
    public static int PassReward = 200;

    private GameInterfaceI gameInterface;

    GoTile(GameInterfaceI gameInterface) {
        this.gameInterface = gameInterface;
    }

    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {
        player.changeBalance(200);
    }

    @Override
    public String tileDescription() {
        return "Name: GO Tile\nDescription: Gain $200 when passing through.";
    }

    @Override
    public boolean tryBuy(Player player) {
        gameInterface.notifyCannotBuyTileKind(player, this);
        return false;
    }

    @Override
    public String getName() {
        return "GO Tile";
    }

    @Override
    public boolean isOwnedBy(Player player) {
        return false;
    }
}
