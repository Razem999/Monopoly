public class GoJail implements GameTileI{

    private GameInterfaceI gameInterface;

    GoJail(GameInterfaceI gameInterface) {
        this.gameInterface = gameInterface;
    }

    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {
        gameBoard.sendPlayerToJail(player);
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
        return "Go To Jail";
    }

    @Override
    public boolean isOwnedBy(Player player) {
        return false;
    }

    @Override
    public boolean tryTransferOwnership(Player newOwner) {
        return false;
    }
}
