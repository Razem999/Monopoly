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
        return "Name: Go To Jail\nDescription: Go to Jail, Do not Pass GO.";
    }

    @Override
    public boolean tryBuy(Player player) {
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
