public class LuxuryTaxTile implements GameTileI{
    public static int baseCost = -100;

    private GameInterfaceI gameInterface;

    LuxuryTaxTile(GameInterfaceI gameInterface) {
        this.gameInterface = gameInterface;
    }

    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {
        player.changeBalance(baseCost);
    }

    @Override
    public String tileDescription() {
        return "Name: Luxury Tax\nDescription: Pay $100";
    }

    @Override
    public boolean tryBuy(Player player) {
        gameInterface.notifyCannotBuyTileKind(player, this);
        return false;
    }

    @Override
    public String getName() {
        return "Luxury Tax";
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
